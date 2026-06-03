import "../../../style.css";
import type { PedidoDto } from "../../../types/api";
import { listOrdersByUser } from "../../../utils/api/orderApi";
import { protegerRutaCliente } from "../../../utils/guards";
import {
  COSTO_ENVIO_DEFAULT,
  formatFechaPedido,
  formatMoney,
  formatOrderId,
  labelFormaPago,
} from "../../../utils/orderFormat";
import { initStoreNav } from "../../../utils/storeNav";

const usuario = protegerRutaCliente();

const abrirDetalle = (pedido: PedidoDto): void => {
  const modal = document.querySelector("#pedido-cliente-modal");
  const titulo = document.querySelector("#pedido-cliente-titulo");
  const body = document.querySelector("#pedido-cliente-body");
  if (!modal || !titulo || !body) return;

  const subtotal = pedido.subtotal ?? pedido.detalles.reduce((s, d) => s + d.subtotal, 0);
  const envio = pedido.costoEnvio ?? COSTO_ENVIO_DEFAULT;

  titulo.textContent = `Pedido ${formatOrderId(pedido.id)}`;
  body.innerHTML = `
    <p><strong>Estado:</strong> <span class="badge-estado badge-${pedido.estado.toLowerCase()}">${pedido.estado}</span></p>
    <p><strong>Fecha:</strong> ${formatFechaPedido(pedido.fecha)}</p>
    <p><strong>Método de pago:</strong> ${labelFormaPago(pedido.formaPago)}</p>
    <p><strong>Teléfono:</strong> ${pedido.telefono ?? "—"}</p>
    <p><strong>Dirección:</strong> ${pedido.direccion ?? "—"}</p>
    <ul>
      ${pedido.detalles.map((d) => `<li>${String(d.cantidad)} x ${d.productoNombre} — ${formatMoney(d.subtotal)}</li>`).join("")}
    </ul>
    <p>Subtotal: ${formatMoney(subtotal)} | Envío: ${formatMoney(envio)}</p>
    <p><strong>Total: ${formatMoney(pedido.total)}</strong></p>
  `;
  modal.removeAttribute("hidden");
};

const renderizarPedidos = (pedidos: PedidoDto[]): void => {
  const contenedor = document.querySelector<HTMLElement>("#lista-pedidos");
  if (!contenedor) return;

  if (pedidos.length === 0) {
    contenedor.innerHTML = "<p>Aún no tenés pedidos.</p>";
    return;
  }

  contenedor.innerHTML = "";
  pedidos.forEach((pedido) => {
    const card = document.createElement("article");
    card.className = "pedido-card";
    card.innerHTML = `
      <h3>${formatOrderId(pedido.id)}</h3>
      <p><span class="badge-estado badge-${pedido.estado.toLowerCase()}">${pedido.estado}</span></p>
      <p><strong>Fecha:</strong> ${formatFechaPedido(pedido.fecha)}</p>
      <p><strong>Total:</strong> ${formatMoney(pedido.total)}</p>
      <p><em>Click para ver detalle</em></p>
    `;
    card.addEventListener("click", () => abrirDetalle(pedido));
    contenedor.appendChild(card);
  });
};

if (usuario) {
  initStoreNav(usuario, "orders");

  if (new URLSearchParams(window.location.search).get("success") === "1") {
    const msg = document.querySelector<HTMLElement>("#mensaje-exito");
    if (msg) {
      msg.textContent = "¡Pedido confirmado correctamente!";
      msg.hidden = false;
    }
  }

  document.querySelector("#cerrar-pedido-modal")?.addEventListener("click", () => {
    document.querySelector("#pedido-cliente-modal")?.setAttribute("hidden", "");
  });
  document.querySelector(".pedido-cliente-backdrop")?.addEventListener("click", () => {
    document.querySelector("#pedido-cliente-modal")?.setAttribute("hidden", "");
  });

  void listOrdersByUser(usuario.id)
    .then(renderizarPedidos)
    .catch(() => {
      window.alert("No se pudieron cargar los pedidos.");
    });
}

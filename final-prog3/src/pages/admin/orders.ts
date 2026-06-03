import { listOrders, updateOrderStatus } from "../../utils/api/orderApi";

import type { BackendEstado, PedidoDto } from "../../types/api";

import {

  escapeHtml,

  formatFechaPedido,

  formatMoney,

  formatOrderId,

  labelFormaPago,

  showError,

} from "./utils";



const contenedor = (): HTMLElement | null => document.querySelector("#lista-pedidos-admin");



const ESTADOS: BackendEstado[] = [

  "PENDIENTE",

  "CONFIRMADO",

  "TERMINADO",

  "CANCELADO",

];



let pedidosCache: PedidoDto[] = [];

let pedidoActivo: PedidoDto | null = null;



const modalEl = (): HTMLElement | null => document.querySelector("#pedido-detalle-modal");

const modalBody = (): HTMLElement | null => document.querySelector("#pedido-modal-body");

const modalTitle = (): HTMLElement | null => document.querySelector("#pedido-modal-title");

const estadoSelect = (): HTMLSelectElement | null =>

  document.querySelector("#pedido-estado-select");



const cerrarModalPedido = (): void => {

  pedidoActivo = null;

  modalEl()?.setAttribute("hidden", "");

};



const abrirModalPedido = (pedido: PedidoDto): void => {

  pedidoActivo = pedido;

  const modal = modalEl();

  const body = modalBody();

  const title = modalTitle();

  const select = estadoSelect();

  if (!modal || !body || !title || !select) return;



  const subtotal = pedido.subtotal ?? pedido.detalles.reduce((s, d) => s + d.subtotal, 0);

  const envio = pedido.costoEnvio ?? 0;



  title.textContent = `Detalle del Pedido ${formatOrderId(pedido.id)}`;



  const lineas = pedido.detalles

    .map(

      (d) => `

      <div class="pedido-item-line">

        <div>

          <strong>${escapeHtml(d.productoNombre)}</strong>

          <p>Cantidad: ${String(d.cantidad)} x ${formatMoney(d.subtotal / d.cantidad)}</p>

        </div>

        <span class="precio">${formatMoney(d.subtotal)}</span>

      </div>`,

    )

    .join("");



  body.innerHTML = `

    <div class="pedido-info-grid">

      <p><strong>Cliente:</strong> ${escapeHtml(pedido.usuarioNombre)}</p>

      <p><strong>Fecha:</strong> ${formatFechaPedido(pedido.fecha)}</p>

      <p><strong>Teléfono:</strong> ${escapeHtml(pedido.telefono ?? "—")}</p>

      <p><strong>Dirección:</strong> ${escapeHtml(pedido.direccion ?? "—")}</p>

      <p><strong>Método de pago:</strong> ${labelFormaPago(pedido.formaPago)}</p>

    </div>

    <div class="pedido-productos">

      <h3>Productos</h3>

      ${lineas}

    </div>

    <div class="pedido-totales">

      <p><span>Subtotal:</span> <span>${formatMoney(subtotal)}</span></p>

      <p><span>Envío:</span> <span>${formatMoney(envio)}</span></p>

      <p class="total-final"><span>Total:</span> <span>${formatMoney(pedido.total)}</span></p>

    </div>

  `;



  select.innerHTML = ESTADOS.map(

    (e) =>

      `<option value="${e}" ${e === pedido.estado ? "selected" : ""}>${e.charAt(0) + e.slice(1).toLowerCase()}</option>`,

  ).join("");



  modal.removeAttribute("hidden");

};



export const renderOrders = async (): Promise<void> => {

  const lista = contenedor();

  if (!lista) return;

  lista.innerHTML = "<p>Cargando pedidos...</p>";



  try {

    pedidosCache = await listOrders();

    aplicarFiltro();

  } catch (error) {

    lista.innerHTML = "<p>Error al cargar pedidos.</p>";

    showError(error);

  }

};



const aplicarFiltro = (): void => {

  const lista = contenedor();

  if (!lista) return;



  const filtro =

    (document.querySelector<HTMLSelectElement>("#filtro-estado")?.value ?? "") as

      | BackendEstado

      | "";



  const pedidos =

    filtro === "" ? pedidosCache : pedidosCache.filter((p) => p.estado === filtro);



  lista.innerHTML = "";

  if (pedidos.length === 0) {

    lista.innerHTML = "<p>No hay pedidos con ese filtro.</p>";

    return;

  }



  pedidos.forEach((pedido) => lista.appendChild(tarjetaPedido(pedido)));

};



const tarjetaPedido = (pedido: PedidoDto): HTMLElement => {

  const card = document.createElement("article");

  card.className = "pedido-list-card";

  const cantidadItems = pedido.detalles.reduce((s, d) => s + d.cantidad, 0);



  card.innerHTML = `

    <h3>${formatOrderId(pedido.id)}</h3>

    <div class="pedido-list-meta">

      <span>${escapeHtml(pedido.usuarioNombre)}</span>

      <span>${formatFechaPedido(pedido.fecha)}</span>

      <span>${String(cantidadItems)} producto${cantidadItems === 1 ? "" : "s"}</span>

      <span class="badge badge-${pedido.estado.toLowerCase()}">${pedido.estado}</span>

    </div>

    <p class="pedido-list-total">${formatMoney(pedido.total)}</p>

  `;



  card.addEventListener("click", () => abrirModalPedido(pedido));

  return card;

};



const actualizarEstado = async (): Promise<void> => {

  if (!pedidoActivo) return;

  const select = estadoSelect();

  if (!select) return;

  const estado = select.value as BackendEstado;

  try {

    await updateOrderStatus(pedidoActivo.id, estado);

    cerrarModalPedido();

    await renderOrders();

    await onOrdersChanged?.();

    window.alert("Estado actualizado correctamente.");

  } catch (error) {

    showError(error);

  }

};



let onOrdersChanged: (() => Promise<void>) | null = null;



export const initOrders = (onChanged: () => Promise<void>): void => {

  onOrdersChanged = onChanged;

  document.querySelector("#filtro-estado")?.addEventListener("change", aplicarFiltro);

  document.querySelector("#btn-actualizar-estado")?.addEventListener("click", () => {

    void actualizarEstado();

  });

  document.querySelector("#pedido-modal-close")?.addEventListener("click", cerrarModalPedido);

  document.querySelector(".pedido-modal-backdrop")?.addEventListener("click", cerrarModalPedido);

};



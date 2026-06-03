import "../../../style.css";
import { createOrder } from "../../../utils/api/orderApi";
import {
  clearCart,
  getCart,
  getCartCount,
  getCartTotal,
  removeFromCart,
  updateCartQuantity,
} from "../../../utils/cart";
import { protegerRutaCliente } from "../../../utils/guards";
import {
  COSTO_ENVIO_DEFAULT,
  formatMoney,
} from "../../../utils/orderFormat";
import { initStoreNav } from "../../../utils/storeNav";
import { navigateTo, ROUTES } from "../../../utils/navigate";

const usuario = protegerRutaCliente();

const actualizarCheckoutTotal = (): void => {
  const subtotal = getCartTotal();
  const total = subtotal + COSTO_ENVIO_DEFAULT;
  const envioEl = document.querySelector("#checkout-envio");
  const totalEl = document.querySelector("#checkout-total");
  if (envioEl) envioEl.textContent = formatMoney(COSTO_ENVIO_DEFAULT);
  if (totalEl) totalEl.textContent = formatMoney(total);
};

const renderizarCarrito = (): void => {
  const contenedor = document.querySelector<HTMLElement>("#contenedor-carrito");
  const totalEl = document.querySelector<HTMLElement>("#carrito-total");
  const btnPago = document.querySelector<HTMLButtonElement>("#btn-proceder-pago");
  if (!contenedor || !totalEl) return;

  const items = getCart();
  if (items.length === 0) {
    contenedor.innerHTML = "<p>Tu carrito está vacío. <a href=\"../home/index.html\">Ir al catálogo</a></p>";
    totalEl.textContent = formatMoney(0);
    if (btnPago) btnPago.hidden = true;
    document.querySelector("#seccion-checkout")?.setAttribute("hidden", "");
    return;
  }

  if (btnPago) btnPago.hidden = false;
  contenedor.innerHTML = "";
  items.forEach((item) => {
    const row = document.createElement("div");
    row.className = "carrito-item";
    row.innerHTML = `
      <img src="${item.imagen}" alt="">
      <div>
        <strong>${item.nombre}</strong>
        <p>${formatMoney(item.precio)} c/u</p>
        <div class="carrito-controles">
          <button type="button" class="btn-menos" data-id="${String(item.productoId)}">-</button>
          <span>${String(item.cantidad)}</span>
          <button type="button" class="btn-mas" data-id="${String(item.productoId)}">+</button>
          <button type="button" class="btn-quitar" data-id="${String(item.productoId)}">Quitar</button>
        </div>
      </div>
    `;
    contenedor.appendChild(row);
  });

  totalEl.textContent = formatMoney(getCartTotal());
  actualizarCheckoutTotal();

  const refresh = (): void => {
    const badge = document.querySelector("#badge-carrito");
    if (badge) badge.textContent = String(getCartCount());
    renderizarCarrito();
  };

  contenedor.querySelectorAll<HTMLButtonElement>(".btn-menos").forEach((btn) => {
    btn.addEventListener("click", () => {
      const error = updateCartQuantity(Number(btn.dataset.id), -1);
      if (error) window.alert(error);
      refresh();
    });
  });
  contenedor.querySelectorAll<HTMLButtonElement>(".btn-mas").forEach((btn) => {
    btn.addEventListener("click", () => {
      const error = updateCartQuantity(Number(btn.dataset.id), 1);
      if (error) window.alert(error);
      refresh();
    });
  });
  contenedor.querySelectorAll<HTMLButtonElement>(".btn-quitar").forEach((btn) => {
    btn.addEventListener("click", () => {
      removeFromCart(Number(btn.dataset.id));
      refresh();
    });
  });
};

if (usuario) {
  initStoreNav(usuario, "cart");
  renderizarCarrito();

  document.querySelector("#btn-proceder-pago")?.addEventListener("click", () => {
    if (getCart().length === 0) {
      window.alert("El carrito está vacío.");
      return;
    }
    document.querySelector("#seccion-checkout")?.removeAttribute("hidden");
    document.querySelector("#seccion-checkout")?.scrollIntoView({ behavior: "smooth" });
    actualizarCheckoutTotal();
  });

  document.querySelector("#btn-volver-carrito")?.addEventListener("click", () => {
    document.querySelector("#seccion-checkout")?.setAttribute("hidden", "");
  });

  document.querySelector<HTMLFormElement>("#form-checkout")?.addEventListener("submit", (e) => {
    e.preventDefault();
    const items = getCart();
    if (items.length === 0) {
      window.alert("El carrito está vacío.");
      return;
    }

    const telefono = document.querySelector<HTMLInputElement>("#checkout-telefono")?.value.trim() ?? "";
    const direccion = document.querySelector<HTMLInputElement>("#checkout-direccion")?.value.trim() ?? "";
    if (!telefono || !direccion) {
      window.alert("Teléfono y dirección son obligatorios.");
      return;
    }

    const formaPago = (
      document.querySelector<HTMLSelectElement>("#forma-pago")?.value ?? "EFECTIVO"
    ) as "TARJETA" | "TRANSFERENCIA" | "EFECTIVO";

    void createOrder({
      estado: "PENDIENTE",
      formaPago,
      idUsuario: usuario.id,
      telefono,
      direccion,
      costoEnvio: COSTO_ENVIO_DEFAULT,
      detallePedido: items.map((item) => ({
        idProducto: item.productoId,
        cantidad: item.cantidad,
      })),
    })
      .then(() => {
        clearCart();
        navigateTo(`${ROUTES.orders()}?success=1`);
      })
      .catch((err: unknown) => {
        const msg = err instanceof Error ? err.message : "Error al crear el pedido.";
        window.alert(msg);
      });
  });
}

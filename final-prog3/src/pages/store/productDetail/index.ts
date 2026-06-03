import "../../../style.css";
import { listProducts } from "../../../utils/api/productApi";
import { addToCart } from "../../../utils/cart";
import { protegerRutaCliente } from "../../../utils/guards";
import { formatMoney } from "../../../utils/orderFormat";
import { initStoreNav } from "../../../utils/storeNav";
import { navigateTo, ROUTES } from "../../../utils/navigate";
import { mapProductoDto } from "../../../types/IProducto";

const usuario = protegerRutaCliente();

const obtenerIdProducto = (): number | null => {
  const id = Number(new URLSearchParams(window.location.search).get("id"));
  return Number.isFinite(id) && id > 0 ? id : null;
};

if (usuario) {
  initStoreNav(usuario, "home");

  void (async () => {
    const id = obtenerIdProducto();
    const contenedor = document.querySelector("#detalle-producto");
    if (!contenedor) return;

    if (id === null) {
      contenedor.innerHTML = "<p>Producto no encontrado.</p>";
      return;
    }

    try {
      const productos = (await listProducts()).map(mapProductoDto);
      const producto = productos.find((p) => p.id === id);
      if (!producto) {
        contenedor.innerHTML = "<p>Producto no encontrado.</p>";
        return;
      }

      const disponible = producto.disponible && producto.stock > 0;
      contenedor.innerHTML = `
        <img src="${producto.imagen}" alt="${producto.nombre}">
        <h2>${producto.nombre}</h2>
        <p>${producto.descripcion}</p>
        <p><strong>Categoría:</strong> ${producto.categoria}</p>
        <p class="producto-precio"><strong>${formatMoney(producto.precio)}</strong></p>
        <p>${disponible ? `Stock: ${String(producto.stock)}` : "No disponible"}</p>
        <div class="detalle-cantidad">
          <label for="cantidad">Cantidad:</label>
          <input type="number" id="cantidad" min="1" max="${String(producto.stock)}" value="1" ${disponible ? "" : "disabled"}>
        </div>
        <button type="button" id="btn-agregar" class="btn-primary" ${disponible ? "" : "disabled"}>Agregar al carrito</button>
        <button type="button" id="btn-ir-carrito" class="btn-secondary">Ir al carrito</button>
      `;

      document.querySelector("#btn-agregar")?.addEventListener("click", () => {
        const cantidad = Number(
          (document.querySelector("#cantidad") as HTMLInputElement)?.value ?? 1,
        );
        const error = addToCart(producto, cantidad);
        if (error) {
          window.alert(error);
          return;
        }
        window.alert(`${String(cantidad)} x ${producto.nombre} agregado al carrito.`);
        navigateTo(ROUTES.cart());
      });

      document.querySelector("#btn-ir-carrito")?.addEventListener("click", () => {
        navigateTo(ROUTES.cart());
      });
    } catch {
      contenedor.innerHTML = "<p>Error al cargar el producto.</p>";
    }
  })();
}

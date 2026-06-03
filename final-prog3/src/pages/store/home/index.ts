import "../../../style.css";
import type { IProducto } from "../../../types/IProducto";
import { listCategories } from "../../../utils/api/categoryApi";
import { listProducts } from "../../../utils/api/productApi";
import { addToCart, getCartCount } from "../../../utils/cart";
import { protegerRutaCliente } from "../../../utils/guards";
import { formatMoney } from "../../../utils/orderFormat";
import { initStoreNav } from "../../../utils/storeNav";
import { navigateTo, ROUTES } from "../../../utils/navigate";
import { mapProductoDto } from "../../../types/IProducto";

const usuario = protegerRutaCliente();
let productosCache: IProducto[] = [];

const slugCategoria = (categoria: string): string =>
  categoria.toLowerCase().replace(/\s+/g, "-");

const crearCardProducto = (
  producto: IProducto,
  idAnclaCategoria?: string,
): HTMLElement => {
  const article = document.createElement("article");
  article.classList.add("producto-card");
  if (idAnclaCategoria) article.id = idAnclaCategoria;

  const disponible = producto.disponible && producto.stock > 0;
  article.innerHTML = `
    <img src="${producto.imagen}" alt="${producto.nombre}">
    <h3>${producto.nombre}</h3>
    <p>${producto.descripcion}</p>
    <p class="producto-precio"><strong>${formatMoney(producto.precio)}</strong></p>
    <p class="producto-stock">${disponible ? `Stock: ${String(producto.stock)}` : "No disponible"}</p>
    <button type="button" class="btn-agregar" ${disponible ? "" : "disabled"}>
      ${disponible ? "Agregar" : "Sin stock"}
    </button>
  `;

  article.addEventListener("click", (e) => {
    if ((e.target as HTMLElement).closest(".btn-agregar")) return;
    navigateTo(ROUTES.productDetail(producto.id));
  });

  article.querySelector<HTMLButtonElement>(".btn-agregar")?.addEventListener("click", (e) => {
    e.stopPropagation();
    const error = addToCart(producto);
    if (error) {
      window.alert(error);
      return;
    }
    const badge = document.querySelector("#badge-carrito");
    if (badge) badge.textContent = String(getCartCount());
    window.alert(`Agregaste al carrito: ${producto.nombre}`);
  });

  return article;
};

const cargarCategorias = (nombres: string[]): void => {
  const lista = document.querySelector<HTMLUListElement>("#lista-categorias");
  if (!lista) return;
  lista.innerHTML = "";
  nombres.forEach((categoria) => {
    const item = document.createElement("li");
    item.innerHTML = `<a href="#${slugCategoria(categoria)}">${categoria}</a>`;
    lista.appendChild(item);
  });
};

const renderizarProductos = (lista: IProducto[]): void => {
  const contenedor = document.querySelector<HTMLElement>("#contenedor-productos");
  if (!contenedor) return;
  contenedor.innerHTML = "";
  const anclas = new Set<string>();
  lista.forEach((producto) => {
    const slug = slugCategoria(producto.categoria);
    const idAncla = anclas.has(slug) ? undefined : slug;
    if (idAncla) anclas.add(slug);
    contenedor.appendChild(crearCardProducto(producto, idAncla));
  });
};

const configurarBusqueda = (): void => {
  document.querySelector<HTMLFormElement>("#form-busqueda")?.addEventListener("submit", (e) => {
    e.preventDefault();
    const termino = (
      document.querySelector<HTMLInputElement>("#busqueda")?.value ?? ""
    )
      .trim()
      .toLowerCase();
    if (!termino) {
      renderizarProductos(productosCache);
      return;
    }
    renderizarProductos(
      productosCache.filter(
        (p) =>
          p.nombre.toLowerCase().includes(termino) ||
          p.descripcion.toLowerCase().includes(termino) ||
          p.categoria.toLowerCase().includes(termino),
      ),
    );
  });
};

if (usuario) {
  initStoreNav(usuario, "home");
  configurarBusqueda();

  void (async () => {
    try {
      const [productosDto, categoriasDto] = await Promise.all([
        listProducts(),
        listCategories(),
      ]);
      productosCache = productosDto.map(mapProductoDto);
      cargarCategorias(categoriasDto.map((c) => c.nombre));

      const destacado = document.querySelector("#producto-destacado");
      if (destacado && productosCache.length > 0) {
        destacado.appendChild(crearCardProducto(productosCache[1] ?? productosCache[0]));
      }
      renderizarProductos(productosCache);
    } catch {
      window.alert("No se pudo cargar el catálogo. Verificá que el backend esté en http://localhost:8080");
    }
  })();
}

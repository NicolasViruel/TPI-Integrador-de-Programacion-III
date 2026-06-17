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
let terminoBusqueda = "";

type OrdenCatalogo = "nombre-asc" | "nombre-desc" | "precio-asc" | "precio-desc";

const slugCategoria = (categoria: string): string =>
  categoria.toLowerCase().replace(/\s+/g, "-");

const obtenerOrden = (): OrdenCatalogo => {
  const valor = document.querySelector<HTMLSelectElement>("#ordenar-productos")?.value;
  if (
    valor === "nombre-desc" ||
    valor === "precio-asc" ||
    valor === "precio-desc"
  ) {
    return valor;
  }
  return "nombre-asc";
};

const ordenarProductos = (lista: IProducto[], orden: OrdenCatalogo): IProducto[] => {
  const copia = [...lista];
  switch (orden) {
    case "nombre-desc":
      return copia.sort((a, b) => b.nombre.localeCompare(a.nombre, "es"));
    case "precio-asc":
      return copia.sort((a, b) => a.precio - b.precio);
    case "precio-desc":
      return copia.sort((a, b) => b.precio - a.precio);
    default:
      return copia.sort((a, b) => a.nombre.localeCompare(b.nombre, "es"));
  }
};

const filtrarProductos = (): IProducto[] => {
  const termino = terminoBusqueda.trim().toLowerCase();
  if (!termino) return productosCache;
  return productosCache.filter(
    (p) =>
      p.nombre.toLowerCase().includes(termino) ||
      p.descripcion.toLowerCase().includes(termino) ||
      p.categoria.toLowerCase().includes(termino),
  );
};

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

const renderizarProductos = (): void => {
  const contenedor = document.querySelector<HTMLElement>("#contenedor-productos");
  if (!contenedor) return;

  const lista = ordenarProductos(filtrarProductos(), obtenerOrden());
  contenedor.innerHTML = "";

  if (lista.length === 0) {
    contenedor.innerHTML = "<p>No hay productos para mostrar.</p>";
    return;
  }

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
    terminoBusqueda = document.querySelector<HTMLInputElement>("#busqueda")?.value ?? "";
    renderizarProductos();
  });
};

const configurarOrden = (): void => {
  document.querySelector<HTMLSelectElement>("#ordenar-productos")?.addEventListener("change", () => {
    renderizarProductos();
  });
};

const configurarSidebarMobile = (): void => {
  const btn = document.querySelector<HTMLButtonElement>("#btn-toggle-sidebar");
  const sidebar = document.querySelector<HTMLElement>("#sidebar-categorias");
  if (!btn || !sidebar) return;

  btn.addEventListener("click", () => {
    sidebar.classList.toggle("sidebar-open");
    document.body.classList.toggle("sidebar-visible");
  });

  sidebar.querySelectorAll("a").forEach((link) => {
    link.addEventListener("click", () => {
      sidebar.classList.remove("sidebar-open");
      document.body.classList.remove("sidebar-visible");
    });
  });
};

if (usuario) {
  initStoreNav(usuario, "home");
  configurarBusqueda();
  configurarOrden();
  configurarSidebarMobile();

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
      renderizarProductos();
    } catch {
      window.alert("No se pudo cargar el catálogo. Verificá que el backend esté en http://localhost:8080");
    }
  })();
}

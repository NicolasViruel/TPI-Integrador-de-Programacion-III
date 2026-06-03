import {
  createProduct,
  deleteProduct,
  listProducts,
  updateProduct,
} from "../../utils/api/productApi";
import type { CategoriaDto, ProductoDto } from "../../types/api";
import { confirmAction, escapeHtml, formatMoney, showError } from "./utils";
import {
  checkboxField,
  closeModal,
  field,
  openModal,
  readFormCheckbox,
  readFormNumber,
  readFormString,
  selectField,
  textareaField,
} from "./modal";

const tbody = (): HTMLTableSectionElement | null =>
  document.querySelector("#tabla-productos tbody");

let categoriasCache: CategoriaDto[] = [];

export const setCategoriesCache = (cats: CategoriaDto[]): void => {
  categoriasCache = cats;
};

export const renderProducts = async (): Promise<void> => {
  const table = tbody();
  if (!table) return;
  table.innerHTML = "<tr><td colspan='8'>Cargando...</td></tr>";

  try {
    const productos = await listProducts();
    table.innerHTML = "";
    if (productos.length === 0) {
      table.innerHTML = "<tr><td colspan='8'>No hay productos.</td></tr>";
      return;
    }
    productos.forEach((p) => table.appendChild(filaProducto(p)));
  } catch (error) {
    table.innerHTML = "<tr><td colspan='8'>Error al cargar productos.</td></tr>";
    showError(error);
  }
};

const filaProducto = (p: ProductoDto): HTMLTableRowElement => {
  const tr = document.createElement("tr");
  tr.innerHTML = `
    <td>${String(p.id)}</td>
    <td><img src="${escapeHtml(p.imagen ?? "")}" alt="" width="48"></td>
    <td>${escapeHtml(p.nombre)}</td>
    <td>${escapeHtml(p.categoriaNombre ?? "—")}</td>
    <td>${formatMoney(p.precio)}</td>
    <td>${String(p.stock)}</td>
    <td>${p.disponible ? "Sí" : "No"}</td>
    <td class="acciones">
      <button type="button" class="btn-editar" data-id="${String(p.id)}">Editar</button>
      <button type="button" class="btn-eliminar" data-id="${String(p.id)}">Eliminar</button>
    </td>
  `;
  tr.querySelector(".btn-editar")?.addEventListener("click", () => abrirEditar(p));
  tr.querySelector(".btn-eliminar")?.addEventListener("click", () => {
    void eliminar(p);
  });
  return tr;
};

const camposProducto = (p?: ProductoDto): string => {
  const catOptions = categoriasCache.map((c) => ({
    value: String(c.id),
    label: c.nombre,
    selected: p?.categoriaId === c.id,
  }));
  if (catOptions.length === 0) {
    catOptions.push({ value: "", label: "Sin categorías", selected: true });
  }

  return `
    ${field("nombre", "Nombre", "text", p?.nombre ?? "", "required")}
    ${textareaField("descripcion", "Descripción", p?.descripcion ?? "")}
    ${field("precio", "Precio", "number", p ? String(p.precio) : "", "required min='0.01' step='0.01'")}
    ${field("stock", "Stock", "number", p ? String(p.stock) : "0", "required min='0'")}
    ${selectField("categoriaId", "Categoría", catOptions)}
    ${field("imagen", "URL imagen", "url", p?.imagen ?? "", "required")}
    ${checkboxField("disponible", "Producto disponible", p?.disponible ?? true)}
  `;
};

const leerProductoForm = (form: HTMLFormElement, stockRequired = true) => {
  const stock = readFormNumber(form, "stock");
  if (stockRequired && (Number.isNaN(stock) || stock < 0)) {
    throw new Error("El stock debe ser mayor o igual a 0.");
  }
  const precio = readFormNumber(form, "precio");
  if (Number.isNaN(precio) || precio <= 0) {
    throw new Error("El precio debe ser mayor a 0.");
  }
  const categoriaId = readFormNumber(form, "categoriaId");
  if (!categoriaId) {
    throw new Error("Seleccioná una categoría.");
  }
  return {
    nombre: readFormString(form, "nombre"),
    descripcion: readFormString(form, "descripcion"),
    precio,
    stock,
    categoriaId,
    imagen: readFormString(form, "imagen"),
    disponible: readFormCheckbox(form, "disponible"),
  };
};

const abrirCrear = (): void => {
  if (categoriasCache.length === 0) {
    window.alert("Creá al menos una categoría antes de agregar productos.");
    return;
  }
  openModal("Nuevo producto", camposProducto(), async (form) => {
    const data = leerProductoForm(form);
    await createProduct(data);
    closeModal();
    await renderProducts();
    await onProductsChanged?.();
  });
};

const abrirEditar = (p: ProductoDto): void => {
  openModal(`Editar: ${p.nombre}`, camposProducto(p), async (form) => {
    const data = leerProductoForm(form);
    await updateProduct(p.id, data);
    closeModal();
    await renderProducts();
    await onProductsChanged?.();
  });
};

const eliminar = async (p: ProductoDto): Promise<void> => {
  if (!confirmAction(`¿Eliminar el producto "${p.nombre}"?`)) return;
  try {
    await deleteProduct(p.id);
    await renderProducts();
    await onProductsChanged?.();
  } catch (error) {
    showError(error);
  }
};

let onProductsChanged: (() => Promise<void>) | null = null;

export const initProducts = (onChanged: () => Promise<void>): void => {
  onProductsChanged = onChanged;
  document.querySelector("#btn-nuevo-producto")?.addEventListener("click", abrirCrear);
};

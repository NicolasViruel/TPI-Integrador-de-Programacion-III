import {
  createCategory,
  deleteCategory,
  listCategories,
  updateCategory,
} from "../../utils/api/categoryApi";
import type { CategoriaDto } from "../../types/api";
import { confirmAction, escapeHtml, showError } from "./utils";
import {
  closeModal,
  field,
  openModal,
  readFormString,
  textareaField,
} from "./modal";

const tbody = (): HTMLTableSectionElement | null =>
  document.querySelector("#tabla-categorias tbody");

export const renderCategories = async (): Promise<void> => {
  const table = tbody();
  if (!table) return;
  table.innerHTML = "<tr><td colspan='5'>Cargando...</td></tr>";

  try {
    const categorias = await listCategories();
    table.innerHTML = "";
    if (categorias.length === 0) {
      table.innerHTML = "<tr><td colspan='5'>No hay categorías.</td></tr>";
      return;
    }
    categorias.forEach((cat) => table.appendChild(filaCategoria(cat)));
  } catch (error) {
    table.innerHTML = "<tr><td colspan='5'>Error al cargar categorías.</td></tr>";
    showError(error);
  }
};

const filaCategoria = (cat: CategoriaDto): HTMLTableRowElement => {
  const tr = document.createElement("tr");
  tr.innerHTML = `
    <td>${String(cat.id)}</td>
    <td><img src="${escapeHtml(cat.imagen ?? "")}" alt="" width="48"></td>
    <td>${escapeHtml(cat.nombre)}</td>
    <td>${escapeHtml(cat.descripcion ?? "")}</td>
    <td class="acciones">
      <button type="button" class="btn-editar" data-id="${String(cat.id)}">Editar</button>
      <button type="button" class="btn-eliminar" data-id="${String(cat.id)}">Eliminar</button>
    </td>
  `;
  tr.querySelector(".btn-editar")?.addEventListener("click", () => abrirEditar(cat));
  tr.querySelector(".btn-eliminar")?.addEventListener("click", () => {
    void eliminar(cat);
  });
  return tr;
};

const abrirCrear = (): void => {
  openModal(
    "Nueva categoría",
    `
      ${field("nombre", "Nombre", "text", "", "required")}
      ${textareaField("descripcion", "Descripción")}
      ${field("imagen", "URL imagen", "url", "", "required")}
    `,
    async (form) => {
      await createCategory({
        nombre: readFormString(form, "nombre"),
        descripcion: readFormString(form, "descripcion"),
        imagen: readFormString(form, "imagen"),
      });
      closeModal();
      await renderCategories();
      await onCategoriesChanged?.();
    },
  );
};

const abrirEditar = (cat: CategoriaDto): void => {
  openModal(
    "Editar categoría",
    `
      ${field("nombre", "Nombre", "text", cat.nombre, "required")}
      ${textareaField("descripcion", "Descripción", cat.descripcion ?? "")}
      ${field("imagen", "URL imagen", "url", cat.imagen ?? "", "required")}
    `,
    async (form) => {
      await updateCategory(cat.id, {
        nombre: readFormString(form, "nombre"),
        descripcion: readFormString(form, "descripcion"),
        imagen: readFormString(form, "imagen"),
      });
      closeModal();
      await renderCategories();
      await onCategoriesChanged?.();
    },
  );
};

const eliminar = async (cat: CategoriaDto): Promise<void> => {
  if (!confirmAction(`¿Eliminar la categoría "${cat.nombre}"?`)) return;
  try {
    await deleteCategory(cat.id);
    await renderCategories();
    await onCategoriesChanged?.();
  } catch (error) {
    showError(error);
  }
};

let onCategoriesChanged: (() => Promise<void>) | null = null;

export const initCategories = (onChanged: () => Promise<void>): void => {
  onCategoriesChanged = onChanged;
  document.querySelector("#btn-nueva-categoria")?.addEventListener("click", abrirCrear);
};

export const getCategoriesForSelect = async (): Promise<CategoriaDto[]> =>
  listCategories();

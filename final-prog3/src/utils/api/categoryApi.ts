import type { CategoriaCreate, CategoriaDto, CategoriaEdit } from "../../types/api";
import { apiFetch } from "./http";

export const listCategories = (): Promise<CategoriaDto[]> =>
  apiFetch<CategoriaDto[]>("/api/categories");

export const createCategory = (body: CategoriaCreate): Promise<CategoriaDto> =>
  apiFetch<CategoriaDto>("/api/categories", {
    method: "POST",
    body: JSON.stringify(body),
  });

export const updateCategory = (
  id: number,
  body: CategoriaEdit,
): Promise<CategoriaDto> =>
  apiFetch<CategoriaDto>(`/api/categories/${String(id)}`, {
    method: "PUT",
    body: JSON.stringify(body),
  });

export const deleteCategory = (id: number): Promise<void> =>
  apiFetch<void>(`/api/categories/${String(id)}`, { method: "DELETE" });

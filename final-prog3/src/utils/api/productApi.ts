import type { ProductoCreate, ProductoDto, ProductoEdit } from "../../types/api";
import { apiFetch } from "./http";

export const listProducts = (): Promise<ProductoDto[]> =>
  apiFetch<ProductoDto[]>("/api/products");

export const listProductsByCategory = (categoryId: number): Promise<ProductoDto[]> =>
  apiFetch<ProductoDto[]>(`/api/products/category/${String(categoryId)}`);

export const createProduct = (body: ProductoCreate): Promise<ProductoDto> =>
  apiFetch<ProductoDto>("/api/products", {
    method: "POST",
    body: JSON.stringify(body),
  });

export const updateProduct = (id: number, body: ProductoEdit): Promise<ProductoDto> =>
  apiFetch<ProductoDto>(`/api/products/${String(id)}`, {
    method: "PUT",
    body: JSON.stringify(body),
  });

export const deleteProduct = (id: number): Promise<void> =>
  apiFetch<void>(`/api/products/${String(id)}`, { method: "DELETE" });

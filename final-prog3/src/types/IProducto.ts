import type { ProductoDto } from "../types/api";

export interface IProducto {
  id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  imagen: string;
  categoria: string;
  stock: number;
  disponible: boolean;
}

export const mapProductoDto = (dto: ProductoDto): IProducto => ({
  id: dto.id,
  nombre: dto.nombre,
  descripcion: dto.descripcion ?? "",
  precio: dto.precio,
  imagen: dto.imagen ?? "",
  categoria: dto.categoriaNombre ?? "Sin categoria",
  stock: dto.stock,
  disponible: dto.disponible ?? false,
});

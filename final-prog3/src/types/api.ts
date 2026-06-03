export type BackendRol = "ADMIN" | "USUARIO";

export type BackendEstado =
  | "PENDIENTE"
  | "CONFIRMADO"
  | "TERMINADO"
  | "CANCELADO";

export type BackendFormaPago = "TARJETA" | "TRANSFERENCIA" | "EFECTIVO";

export interface UsuarioDto {
  id: number;
  nombre: string;
  apellido: string;
  mail: string;
  celular: string | null;
  rol: BackendRol;
  nombreCompleto: string;
}

export interface CategoriaDto {
  id: number;
  nombre: string;
  descripcion: string;
  imagen: string;
  cantidadProductos: number;
}

export interface CategoriaCreate {
  nombre: string;
  descripcion: string;
  imagen: string;
}

export interface CategoriaEdit {
  nombre?: string;
  descripcion?: string;
  imagen?: string;
}

export interface ProductoCreate {
  nombre: string;
  precio: number;
  descripcion: string;
  stock: number;
  imagen: string;
  disponible: boolean;
  categoriaId: number;
}

export interface ProductoEdit {
  nombre?: string;
  precio?: number;
  descripcion?: string;
  stock: number;
  imagen?: string;
  disponible?: boolean;
  categoriaId?: number;
}

export interface ProductoDto {
  id: number;
  nombre: string;
  precio: number;
  descripcion: string;
  stock: number;
  imagen: string;
  disponible: boolean;
  categoriaId: number | null;
  categoriaNombre: string | null;
}

export interface DetallePedidoDto {
  id: number;
  cantidad: number;
  subtotal: number;
  productoId: number;
  productoNombre: string;
}

export interface PedidoDto {
  id: number;
  fecha: string;
  estado: BackendEstado;
  total: number;
  formaPago: BackendFormaPago;
  usuarioId: number;
  usuarioNombre: string;
  telefono?: string | null;
  direccion?: string | null;
  costoEnvio?: number | null;
  subtotal?: number | null;
  detalles: DetallePedidoDto[];
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  nombre: string;
  apellido: string;
  email: string;
  celular?: string;
  password: string;
}

export interface PedidoCreateRequest {
  estado: BackendEstado;
  formaPago: BackendFormaPago;
  idUsuario: number;
  telefono?: string;
  direccion?: string;
  costoEnvio?: number;
  detallePedido: { idProducto: number; cantidad: number }[];
}

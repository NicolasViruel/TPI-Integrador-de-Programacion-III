import type { IProducto } from "../types/IProducto";

export interface CartItem {
  productoId: number;
  nombre: string;
  precio: number;
  imagen: string;
  cantidad: number;
  stock: number;
}

const STORAGE_CART = "foodstore_cart";

const parseCart = (raw: string | null): CartItem[] => {
  if (raw === null) return [];
  try {
    const data: unknown = JSON.parse(raw);
    return Array.isArray(data) ? (data as CartItem[]) : [];
  } catch {
    return [];
  }
};

export const getCart = (): CartItem[] => parseCart(localStorage.getItem(STORAGE_CART));

const saveCart = (items: CartItem[]): void => {
  localStorage.setItem(STORAGE_CART, JSON.stringify(items));
};

export const getCartCount = (): number =>
  getCart().reduce((total, item) => total + item.cantidad, 0);

export const addToCart = (producto: IProducto, cantidad = 1): string | null => {
  if (!producto.disponible || producto.stock <= 0) {
    return "Este producto no está disponible.";
  }

  const cart = getCart();
  const existente = cart.find((item) => item.productoId === producto.id);
  const cantidadActual = existente?.cantidad ?? 0;
  const nuevaCantidad = cantidadActual + cantidad;

  if (nuevaCantidad > producto.stock) {
    return `Stock insuficiente. Disponible: ${String(producto.stock)}`;
  }

  if (existente) {
    existente.cantidad = nuevaCantidad;
  } else {
    cart.push({
      productoId: producto.id,
      nombre: producto.nombre,
      precio: producto.precio,
      imagen: producto.imagen,
      cantidad,
      stock: producto.stock,
    });
  }

  saveCart(cart);
  return null;
};

export const updateCartQuantity = (productoId: number, delta: number): string | null => {
  const cart = getCart();
  const item = cart.find((i) => i.productoId === productoId);
  if (!item) return "Producto no encontrado en el carrito.";

  const nueva = item.cantidad + delta;
  if (nueva <= 0) {
    saveCart(cart.filter((i) => i.productoId !== productoId));
    return null;
  }
  if (nueva > item.stock) {
    return `Stock insuficiente. Disponible: ${String(item.stock)}`;
  }
  item.cantidad = nueva;
  saveCart(cart);
  return null;
};

export const removeFromCart = (productoId: number): void => {
  saveCart(getCart().filter((i) => i.productoId !== productoId));
};

export const clearCart = (): void => {
  localStorage.removeItem(STORAGE_CART);
};

export const getCartTotal = (): number =>
  getCart().reduce((total, item) => total + item.precio * item.cantidad, 0);

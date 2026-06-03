import type {
  BackendEstado,
  PedidoCreateRequest,
  PedidoDto,
} from "../../types/api";
import { apiFetch } from "./http";

export const listOrders = (): Promise<PedidoDto[]> =>
  apiFetch<PedidoDto[]>("/api/orders");

export const listOrdersByUser = (userId: number): Promise<PedidoDto[]> =>
  apiFetch<PedidoDto[]>(`/api/orders/user/${String(userId)}`);

export const createOrder = (body: PedidoCreateRequest): Promise<PedidoDto> =>
  apiFetch<PedidoDto>("/api/orders", {
    method: "POST",
    body: JSON.stringify(body),
  });

export const updateOrderStatus = (
  id: number,
  estado: BackendEstado,
): Promise<PedidoDto> =>
  apiFetch<PedidoDto>(`/api/orders/${String(id)}/status`, {
    method: "PATCH",
    body: JSON.stringify({ estado }),
  });

export const deleteOrder = (id: number): Promise<void> =>
  apiFetch<void>(`/api/orders/${String(id)}`, { method: "DELETE" });

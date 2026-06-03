import type { LoginRequest, RegisterRequest, UsuarioDto } from "../../types/api";
import { apiFetch } from "./http";

export const login = (body: LoginRequest): Promise<UsuarioDto> =>
  apiFetch<UsuarioDto>("/api/auth/login", {
    method: "POST",
    body: JSON.stringify(body),
  });

export const register = (body: RegisterRequest): Promise<UsuarioDto> =>
  apiFetch<UsuarioDto>("/api/auth/register", {
    method: "POST",
    body: JSON.stringify(body),
  });

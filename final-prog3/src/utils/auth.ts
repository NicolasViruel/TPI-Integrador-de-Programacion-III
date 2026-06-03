import * as authApi from "./api/authApi";
import { ApiError } from "./api/http";
import type { IUser } from "../types/IUser";
import { mapUsuarioDto } from "./mapUser";

const STORAGE_SESSION = "userData";

const isIUser = (value: unknown): value is IUser => {
  if (typeof value !== "object" || value === null) return false;
  const o = value as Record<string, unknown>;
  return (
    typeof o.id === "number" &&
    typeof o.email === "string" &&
    typeof o.nombre === "string" &&
    typeof o.apellido === "string" &&
    (o.rol === "admin" || o.rol === "client")
  );
};

export const getSession = (): IUser | null => {
  const raw = localStorage.getItem(STORAGE_SESSION);
  if (raw === null) return null;
  try {
    const data: unknown = JSON.parse(raw);
    return isIUser(data) ? data : null;
  } catch {
    return null;
  }
};

export const setSession = (user: IUser): void => {
  localStorage.setItem(STORAGE_SESSION, JSON.stringify(user));
};

export const clearSession = (): void => {
  localStorage.removeItem(STORAGE_SESSION);
};

export type AuthResult =
  | { ok: true; user: IUser }
  | { ok: false; error: string };

export const iniciarSesion = async (
  email: string,
  password: string,
): Promise<AuthResult> => {
  try {
    const dto = await authApi.login({ email: email.trim(), password });
    const user = mapUsuarioDto(dto);
    return { ok: true, user };
  } catch (error) {
    if (error instanceof ApiError) {
      return { ok: false, error: error.message };
    }
    return { ok: false, error: "No se pudo conectar con el servidor." };
  }
};

export type RegistroResult =
  | { ok: true; user: IUser }
  | { ok: false; error: string };

export const registrarUsuario = async (data: {
  nombre: string;
  apellido: string;
  email: string;
  password: string;
  celular?: string;
}): Promise<RegistroResult> => {
  try {
    const dto = await authApi.register({
      nombre: data.nombre.trim(),
      apellido: data.apellido.trim(),
      email: data.email.trim(),
      password: data.password,
      celular: data.celular?.trim() || undefined,
    });
    const user = mapUsuarioDto(dto);
    return { ok: true, user };
  } catch (error) {
    if (error instanceof ApiError) {
      return { ok: false, error: error.message };
    }
    return { ok: false, error: "No se pudo conectar con el servidor." };
  }
};

export const cerrarSesion = (): void => {
  clearSession();
};

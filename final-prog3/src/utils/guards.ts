import type { IUser } from "../types/IUser";
import { Rol } from "../types/Rol";
import { getSession } from "./auth";
import { navigateTo, ROUTES } from "./navigate";

export const protegerRutaAdministracion = (): IUser | null => {
  const user = getSession();
  if (!user) {
    navigateTo(ROUTES.login());
    return null;
  }
  if (user.rol !== Rol.Admin) {
    navigateTo(ROUTES.storeHome());
    return null;
  }
  return user;
};

export const protegerRutaCliente = (): IUser | null => {
  const user = getSession();
  if (!user) {
    navigateTo(ROUTES.login());
    return null;
  }
  return user;
};

export const redirigirSiSesionActiva = (): void => {
  const user = getSession();
  if (user) {
    navigateTo(user.rol === Rol.Admin ? ROUTES.admin() : ROUTES.storeHome());
  }
};

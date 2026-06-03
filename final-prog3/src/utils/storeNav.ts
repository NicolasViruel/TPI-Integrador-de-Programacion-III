import type { IUser } from "../types/IUser";
import { Rol } from "../types/Rol";
import { getCartCount } from "./cart";
import { cerrarSesion } from "./auth";
import { navigateTo, ROUTES } from "./navigate";

export const initStoreNav = (user: IUser, active: "home" | "cart" | "orders"): void => {
  const saludo = document.querySelector("#saludo-usuario");
  const navAdmin = document.querySelector<HTMLElement>("#nav-admin");
  const badge = document.querySelector("#badge-carrito");

  if (saludo) saludo.textContent = `Hola, ${user.nombre} (${user.email}).`;
  if (user.rol === Rol.Admin && navAdmin) navAdmin.hidden = false;
  if (badge) badge.textContent = String(getCartCount());

  document.querySelectorAll<HTMLElement>("[data-nav]").forEach((el) => {
    el.classList.toggle("nav-active", el.dataset.nav === active);
  });

  document.querySelector("#btn-cerrar-sesion")?.addEventListener("click", () => {
    cerrarSesion();
    navigateTo(ROUTES.login());
  });
};

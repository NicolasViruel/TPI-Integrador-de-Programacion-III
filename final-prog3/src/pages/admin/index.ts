import "./admin.css";
import { listCategories } from "../../utils/api/categoryApi";
import { cerrarSesion } from "../../utils/auth";
import { navigateTo, ROUTES } from "../../utils/navigate";
import { protegerRutaAdministracion } from "../../utils/guards";
import { initCategories, renderCategories } from "./categories";
import { renderDashboard } from "./dashboard";
import { initModal } from "./modal";
import { initOrders, renderOrders } from "./orders";
import { initProducts, renderProducts, setCategoriesCache } from "./products";

const usuario = protegerRutaAdministracion();

const refreshAll = async (): Promise<void> => {
  const cats = await listCategories();
  setCategoriesCache(cats);
  await Promise.all([renderDashboard(), renderCategories(), renderProducts(), renderOrders()]);
};

const showTab = (tabId: string): void => {
  document.querySelectorAll<HTMLElement>(".admin-panel").forEach((panel) => {
    panel.hidden = panel.id !== `tab-${tabId}`;
  });
  document.querySelectorAll<HTMLElement>(".sidebar-link[data-tab]").forEach((btn) => {
    btn.classList.toggle("active", btn.dataset.tab === tabId);
  });
};

const initNavigation = (): void => {
  document.querySelectorAll<HTMLElement>(".sidebar-link[data-tab]").forEach((btn) => {
    btn.addEventListener("click", () => {
      const tab = btn.dataset.tab;
      if (tab) showTab(tab);
    });
  });

  document.querySelectorAll<HTMLButtonElement>(".btn-gestionar[data-goto]").forEach((btn) => {
    btn.addEventListener("click", () => {
      const tab = btn.dataset.goto;
      if (tab) showTab(tab);
    });
  });
};

document.querySelector("#btn-cerrar-sesion")?.addEventListener("click", () => {
  cerrarSesion();
  navigateTo(ROUTES.login());
});

if (usuario) {
  const userName = document.querySelector("#admin-user-name");
  if (userName) {
    userName.textContent = usuario.nombre;
  }

  initModal();
  initNavigation();
  initCategories(refreshAll);
  initProducts(refreshAll);
  initOrders(refreshAll);

  void refreshAll();
}

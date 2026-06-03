import { Rol } from "../../../types/Rol";
import { iniciarSesion, setSession } from "../../../utils/auth";
import { navigateTo, ROUTES } from "../../../utils/navigate";
import { redirigirSiSesionActiva } from "../../../utils/guards";

redirigirSiSesionActiva();

const formulario = document.querySelector<HTMLFormElement>("#formulario-login");
const mensaje = document.querySelector<HTMLParagraphElement>("#mensaje");
const btnSubmit = formulario?.querySelector<HTMLButtonElement>("button[type=submit]");

const mostrarMensaje = (texto: string, esOk: boolean): void => {
  if (!mensaje) return;
  mensaje.hidden = false;
  mensaje.textContent = texto;
  mensaje.classList.toggle("ok", esOk);
};

formulario?.addEventListener("submit", (evento) => {
  evento.preventDefault();
  const datos = new FormData(formulario);
  const email = String(datos.get("email") ?? "");
  const password = String(datos.get("password") ?? "");

  if (btnSubmit) btnSubmit.disabled = true;

  void iniciarSesion(email, password).then((resultado) => {
    if (btnSubmit) btnSubmit.disabled = false;
    if (!resultado.ok) {
      mostrarMensaje(resultado.error, false);
      return;
    }
    setSession(resultado.user);
    navigateTo(
      resultado.user.rol === Rol.Admin ? ROUTES.admin() : ROUTES.storeHome(),
    );
  });
});

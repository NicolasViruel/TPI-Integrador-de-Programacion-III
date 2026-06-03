import { showError } from "./utils";

export type ModalSubmitHandler = (form: HTMLFormElement) => void | Promise<void>;

let onSubmit: ModalSubmitHandler | null = null;

const modal = (): HTMLElement | null => document.querySelector("#admin-modal");
const modalTitle = (): HTMLElement | null => document.querySelector("#modal-title");
const modalForm = (): HTMLFormElement | null =>
  document.querySelector("#modal-form");
const modalFields = (): HTMLElement | null => document.querySelector("#modal-fields");

export const closeModal = (): void => {
  modal()?.setAttribute("hidden", "");
  onSubmit = null;
  modalForm()?.reset();
  const fields = modalFields();
  if (fields) fields.innerHTML = "";
};

export const openModal = (
  title: string,
  fieldsHtml: string,
  submitHandler: ModalSubmitHandler,
): void => {
  const titleEl = modalTitle();
  const fieldsEl = modalFields();
  if (titleEl) titleEl.textContent = title;
  if (fieldsEl) fieldsEl.innerHTML = fieldsHtml;
  onSubmit = submitHandler;
  modal()?.removeAttribute("hidden");
};

export const initModal = (): void => {
  document.querySelector("#modal-cancel")?.addEventListener("click", closeModal);
  document.querySelector("#modal-backdrop")?.addEventListener("click", closeModal);

  modalForm()?.addEventListener("submit", (event) => {
    event.preventDefault();
    const form = modalForm();
    if (!form || !onSubmit) return;
    void Promise.resolve(onSubmit(form))
      .then(() => undefined)
      .catch(showError);
  });
};

export const field = (
  id: string,
  label: string,
  type: string,
  value = "",
  extra = "",
): string => `
  <label for="${id}">${label}</label>
  <input type="${type}" id="${id}" name="${id}" value="${value.replace(/"/g, "&quot;")}" ${extra}>
`;

export const textareaField = (
  id: string,
  label: string,
  value = "",
): string => `
  <label for="${id}">${label}</label>
  <textarea id="${id}" name="${id}" rows="3">${value.replace(/</g, "&lt;")}</textarea>
`;

export const selectField = (
  id: string,
  label: string,
  options: { value: string; label: string; selected?: boolean }[],
): string => {
  const opts = options
    .map(
      (o) =>
        `<option value="${o.value}" ${o.selected ? "selected" : ""}>${o.label}</option>`,
    )
    .join("");
  return `
    <label for="${id}">${label}</label>
    <select id="${id}" name="${id}">${opts}</select>
  `;
};

export const checkboxField = (
  id: string,
  label: string,
  checked: boolean,
): string => `
  <label class="checkbox-row">
    <input type="checkbox" id="${id}" name="${id}" ${checked ? "checked" : ""}>
    ${label}
  </label>
`;

export const readFormString = (form: HTMLFormElement, name: string): string =>
  String(new FormData(form).get(name) ?? "").trim();

export const readFormNumber = (form: HTMLFormElement, name: string): number =>
  Number(readFormString(form, name));

export const readFormCheckbox = (form: HTMLFormElement, name: string): boolean =>
  form.querySelector<HTMLInputElement>(`[name="${name}"]`)?.checked ?? false;

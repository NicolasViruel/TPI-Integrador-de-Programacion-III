export const escapeHtml = (text: string): string =>
  text
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;");

export const formatMoney = (value: number): string =>
  `$${value.toLocaleString("es-AR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;

export const formatOrderId = (id: number): string => `ORD-${String(id)}`;

export const formatFechaPedido = (fechaIso: string): string => {
  const d = new Date(`${fechaIso}T12:00:00`);
  return d.toLocaleDateString("es-AR", {
    day: "numeric",
    month: "long",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

export const labelFormaPago = (forma: string): string => {
  const map: Record<string, string> = {
    TARJETA: "Tarjeta",
    TRANSFERENCIA: "Transferencia",
    EFECTIVO: "Efectivo",
  };
  return map[forma] ?? forma;
};

export const showError = (error: unknown): void => {
  const msg = error instanceof Error ? error.message : "Ocurrió un error inesperado.";
  window.alert(msg);
};

export const confirmAction = (message: string): boolean => window.confirm(message);

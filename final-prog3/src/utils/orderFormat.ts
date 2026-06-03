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

export const COSTO_ENVIO_DEFAULT = 500;

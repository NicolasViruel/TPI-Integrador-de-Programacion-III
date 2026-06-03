import { listCategories } from "../../utils/api/categoryApi";

import { listProducts } from "../../utils/api/productApi";

import { listOrders } from "../../utils/api/orderApi";

import type { BackendEstado } from "../../types/api";

import { formatMoney } from "./utils";



export const renderDashboard = async (): Promise<void> => {

  const resumen = document.querySelector("#resumen-dashboard");

  try {

    const [categorias, productos, pedidos] = await Promise.all([

      listCategories(),

      listProducts(),

      listOrders(),

    ]);



    const set = (id: string, value: string): void => {

      const el = document.querySelector(`#${id}`);

      if (el) el.textContent = value;

    };



    const countEstado = (estado: BackendEstado): number =>

      pedidos.filter((p) => p.estado === estado).length;



    const disponibles = productos.filter((p) => p.disponible && p.stock > 0).length;



    set("stat-categorias", String(categorias.length));

    set("stat-productos", String(productos.length));

    set("stat-pedidos", String(pedidos.length));

    set("stat-disponibles", String(disponibles));



    if (resumen) {

      resumen.innerHTML = `

        <p>Pedidos pendientes: <strong>${String(countEstado("PENDIENTE"))}</strong></p>

        <p>Pedidos confirmados: <strong>${String(countEstado("CONFIRMADO"))}</strong></p>

        <p>Pedidos terminados: <strong>${String(countEstado("TERMINADO"))}</strong></p>

        <p>Pedidos cancelados: <strong>${String(countEstado("CANCELADO"))}</strong></p>

        <p>Ingresos acumulados (todos los pedidos): <strong>${formatMoney(pedidos.reduce((s, p) => s + p.total, 0))}</strong></p>

      `;

    }

  } catch {

    if (resumen) {

      resumen.innerHTML =

        "<p>No se pudo cargar el resumen. Verificá que el backend esté activo en http://localhost:8080</p>";

    }

  }

};


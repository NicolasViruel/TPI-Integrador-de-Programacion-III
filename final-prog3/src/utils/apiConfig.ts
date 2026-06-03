/** En dev Vite proxyea /api → localhost:8080. En build estático apunta al backend. */
export const API_BASE = import.meta.env.DEV
  ? ""
  : (import.meta.env.VITE_API_URL ?? "http://localhost:8080");

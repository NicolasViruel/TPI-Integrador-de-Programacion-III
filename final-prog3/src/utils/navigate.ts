const base = (): string => import.meta.env.BASE_URL;

export const ROUTES = {
  root: () => `${base()}index.html`,
  login: () => `${base()}src/pages/auth/login/index.html`,
  registro: () => `${base()}src/pages/auth/registro/index.html`,
  admin: () => `${base()}src/pages/admin/index.html`,
  storeHome: () => `${base()}src/pages/store/home/index.html`,
  productDetail: (id: number) =>
    `${base()}src/pages/store/productDetail/index.html?id=${String(id)}`,
  cart: () => `${base()}src/pages/store/cart/index.html`,
  orders: () => `${base()}src/pages/client/orders/index.html`,
} as const;

export const navigateTo = (url: string): void => {
  window.location.assign(url);
};

import { defineConfig } from "vite";

export default defineConfig({
  appType: "mpa",
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
    },
  },
  build: {
    rollupOptions: {
      input: {
        main: "index.html",
        login: "src/pages/auth/login/index.html",
        registro: "src/pages/auth/registro/index.html",
        admin: "src/pages/admin/index.html",
        storeHome: "src/pages/store/home/index.html",
        productDetail: "src/pages/store/productDetail/index.html",
        cart: "src/pages/store/cart/index.html",
        orders: "src/pages/client/orders/index.html",
      },
    },
  },
});

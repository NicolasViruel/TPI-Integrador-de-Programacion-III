# Food Store — Frontend

Proyecto TypeScript + Vite (MPA), estructura según consigna Programación III.

## Estructura

```
final-prog3/
├── index.html              # Redirección a login
├── package.json
├── tsconfig.json
├── vite.config.ts
├── public/assets/          # Imágenes estáticas (servidas por Vite)
└── src/
    ├── main.ts             # Punto de entrada vacío
    ├── style.css           # Estilos globales
    ├── types/
    ├── utils/
    └── pages/
        ├── auth/
        ├── store/
        │   ├── home/
        │   ├── productDetail/
        │   └── cart/
        ├── client/
        │   └── orders/
        └── admin/
```

## Scripts

```bash
npm install
npm run dev      # http://localhost:5173
npm run build
```

Requiere el backend en `../foodstore-backend` (`./gradlew bootRun`).

# Food Store — TPI Programación III

**Universidad Nacional de Tucumán — Programación III**

Sistema full-stack de tienda online: catálogo de productos, carrito de compras, checkout, seguimiento de pedidos y panel de administración para categorías, productos y estados de pedido.

---

## Entrega y documentación (obligatorio)

### Video demostrativo

Enlace al video con permisos **públicos** de visualización:

**[Ver video demostrativo en YouTube](https://youtu.be/n3_Yjvu54CA)**

### Documentación PDF

Documento del TPI según las consignas de la cátedra:

- **Opción A (enlace):** [Abrir documentación PDF](https://TU-ENLACE-AL-PDF-AQUI)
- **Opción B (archivo en el repo):** [`Documentacion_TPI_FoodStore.pdf`](./Documentacion_TPI_FoodStore.pdf)



---

## Integrantes

| Nombre | Legajo / DNI |
|--------|----------------|
| *Nicolas* | *Viruel* |

**Comisión / Año:** *10 / 2026*

---

## Descripción del proyecto

**Food Store** es una aplicación web que simula una tienda de comidas. Los **clientes** pueden registrarse, navegar el catálogo, armar un carrito, completar el checkout y consultar el estado de sus pedidos. Los **administradores** gestionan categorías, productos y pedidos desde un panel dedicado.

### Funcionalidades principales

| Área | Funcionalidad |
|------|----------------|
| **Autenticación** | Login, registro de nuevos usuarios, roles Admin / Cliente |
| **Tienda** | Catálogo, búsqueda, detalle de producto, carrito, checkout |
| **Cliente** | Mis pedidos con estado y detalle |
| **Administración** | Dashboard, CRUD categorías y productos, gestión y actualización de pedidos |

### Stack tecnológico

| Capa | Tecnología |
|------|------------|
| Frontend | TypeScript, Vite (MPA), HTML, CSS |
| Backend | Java 17, Spring Boot 3.2, Spring Data JPA |
| Base de datos | H2 (archivo embebido, fines educativos) |
| API | REST + documentación OpenAPI (Swagger) |

---

## Estructura del repositorio

```
.
├── README.md                    ← Este archivo (presentación del TPI)
├── Documentacion_TPI_FoodStore.pdf   ← (opcional) PDF en la raíz del repo
├── final-prog3/                 ← Frontend (TypeScript + Vite)
│   └── README.md                ← Instalación y rutas del frontend
└── foodstore-backend/           ← Backend (Spring Boot)
    └── README.md                ← Instalación y endpoints de la API
```

---

## Requisitos previos

- **JDK 17** o superior
- **Node.js 18** o superior

---

## Cómo ejecutar el proyecto en local

### 1. Backend (puerto 8080)

```bash
cd foodstore-backend
./gradlew bootRun
```

En Windows: `gradlew.bat bootRun`

- API: http://localhost:8080  
- Swagger: http://localhost:8080/swagger-ui.html  

### 2. Frontend (puerto 5173)

En otra terminal:

```bash
cd final-prog3
npm install
npm run dev
```

Abrir en el navegador: http://localhost:5173/src/pages/auth/login/index.html

El frontend en modo desarrollo envía las peticiones `/api` al backend mediante proxy de Vite.

### Configuración de la base de datos

El backend usa **H2 en modo archivo** (`jdbc:h2:file:./data/pedidos_db`). Los datos persisten en `foodstore-backend/data/`.

---

## Usuarios de prueba

Cargados automáticamente al iniciar el backend:

| Rol | Email | Contraseña |
|-----|-------|------------|
| Administrador | `admin@admin.com` | `123456` |
| Cliente | `cliente@foodstore.com` | `usuario123` |

También es posible **registrar un nuevo usuario** desde la pantalla de login.

---

## Consideraciones de seguridad (educativo)

Proyecto con fines **académicos**:

- No implementa JWT; la sesión se guarda en `localStorage` en el frontend.
- La validación de rol de administrador es principalmente del lado cliente.
- Las contraseñas se almacenan hasheadas con **BCrypt** en el backend.

---

## Documentación adicional

- Detalle del frontend: [final-prog3/README.md](./final-prog3/README.md)
- Detalle del backend: [foodstore-backend/README.md](./foodstore-backend/README.md)

---

## Link Video: 

https://studio.youtube.com/video/iVp81hHnj7U/edit

---

## Licencia y uso

Proyecto académico — TPI Programación III, UNT. Uso educativo.

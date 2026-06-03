# Food Store — Backend

API REST Spring Boot 3.2 (Java 17, H2).

## Estructura

```
foodstore-backend/
├── build.gradle
├── settings.gradle
├── gradlew / gradlew.bat
├── gradle/wrapper/
└── src/main/java/com/tuuniversidad/foodstore/
    ├── FoodstoreApplication.java
    ├── model/
    ├── repository/
    ├── service/impl/       # Lógica de negocio + mappers
    ├── controller/
    ├── dto/
    └── exception/
```

Configuración adicional (CORS, OpenAPI, datos de prueba): clases en el paquete raíz `com.tuuniversidad.foodstore`.

## Ejecutar

```bash
./gradlew bootRun
```

API: http://localhost:8080 — Swagger: http://localhost:8080/swagger-ui.html

Usuarios de prueba: `admin@foodstore.com` / `admin123`, `cliente@foodstore.com` / `usuario123`

Frontend (Vite): carpeta hermana `../final-prog3` — `npm run dev` en ese directorio.

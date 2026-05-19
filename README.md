# 🧾 Order System App

Aplicación **fullstack** para la gestión de productos y órdenes con autenticación segura, roles de usuario y dashboard de estadísticas.

✅ Proyecto funcional desplegado en producción con mejoras continuas

---

## ✨ Features principales

* 🔐 Autenticación con JWT
* 👤 Registro e inicio de sesión de usuarios
* 🛡️ Control de roles (USER / ADMIN)
* 🛒 Carrito de compras dinámico
* 📦 Creación y gestión de órdenes
* 🏷️ CRUD completo de productos (solo ADMIN)
* 📊 Dashboard con estadísticas de ventas
* 🔎 Búsqueda de productos y órdenes
* ⚡ Interfaz moderna y responsive
* 💰 Precios por producto y total por orden
* 📄 Paginación en productos y órdenes

---

## 🚀 Tecnologías utilizadas

### 🔹 Backend

* Java
* Spring Boot
* Spring Security
* JWT (JSON Web Tokens)
* Spring Data JPA / Hibernate
* PostgreSQL
* Arquitectura REST
* Validaciones con Jakarta Validation
* Paginación 

---

### 🔹 Frontend

* React
* JavaScript (ES6+)
* Tailwind CSS
* Axios
* React Context API
* React Router DOM
* Recharts (gráficos)
* React Hot Toast (notificaciones)

---

## 🏗️ Arquitectura

El proyecto sigue una arquitectura en capas:

* **Controller** → Manejo de endpoints REST
* **Service** → Lógica de negocio
* **Repository** → Acceso a datos (JPA)

🧩 Componentes de soporte

* **DTO** → Transferencia de datos entre capas
* **Mapper** → Conversión entre entidades y DTOs
* **Model** → Entidades JPA (User, Product, Order)
* **Security** → JWT + filtros + roles
* **Exception** → Manejo centralizado de errores (GlobalExceptionHandler)

---

## 📁 Estructura del proyecto

```bash
ordersystem/
├── backend/   # API REST con Spring Boot
├── frontend/  # Aplicación en React
```

---

## ▶️ Cómo probar el proyecto

El proyecto ya está desplegado y se puede usar directamente sin necesidad de instalar nada localmente:

* Frontend (Vercel): https://order-system-vert.vercel.app
* Backend (Render): https://ordersystem-backend.onrender.com

💡 Cuentas de ejemplo:

ADMIN
+ Usuario: admin
+ Contraseña: 123456

Con esta cuenta se puede acceder a todas las funcionalidades de administrador.

USER
+ Usuario: mateo
+ Contraseña: 123456

Con esta cuenta se puede acceder a todas las funcionalidades de usuario o pueden registrarse con cualquier email y contraseña.

---

## 🔐 Seguridad

* Autenticación basada en JWT
* Contraseñas encriptadas con BCrypt
* Filtro personalizado (`JwtFilter`)
* Control de acceso por roles:

  * ADMIN → gestión completa
  * USER → compras y órdenes

---

## 📊 Funcionalidades destacadas

### 👤 Usuario

* Registro y login
* Visualización de productos
* Agregar productos al carrito
* Crear órdenes
* Ver historial de compras

### 🛠️ Administrador

* Crear / editar / eliminar productos
* Ver todas las órdenes
* Dashboard con estadísticas

---

### 🧪 Testing

El proyecto incluye diferentes niveles de testing:

- Unit Tests → lógica de negocio (services)
- Controller Tests → validación de endpoints con MockMvc
- Integration Tests → flujo completo con seguridad, base de datos y autenticación real

---

## 📚 Aprendizajes

- Diseño e implementación de APIs REST con Spring Boot
- Autenticación y autorización con JWT y manejo de roles (USER/ADMIN)
- Arquitectura en capas (controller, service, repository) con buenas prácticas (DTOs, mappers, utils)
- Testing en múltiples capas: unitarios, controllers y tests de integración
- Documentación y prueba de endpoints con Postman
- Consumo de APIs REST desde React 
- Deploy fullstack con Vercel (frontend) y Render (backend)

---

## 👨‍💻 Autor

**Mateo Calcagno**

---


# 🧾 Order System App

Aplicación **fullstack** para la gestión de productos y órdenes con autenticación segura, roles de usuario y dashboard de estadísticas.

🚧 Proyecto en desarrollo activo (mejoras continuas y nuevas funcionalidades)

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
* **DTOs** → Transferencia de datos
* **Security** → JWT + filtros + roles

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

💡 Cuenta de administrador de ejemplo:

+ Usuario: admin
+ Contraseña: 1234

Con esta cuenta se puede acceder a todas las funcionalidades de administrador.

Para probar como usuario normal, pueden registrarse con cualquier email y contraseña.

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

## 🎯 Objetivo del proyecto

Este proyecto fue desarrollado para:

* Practicar desarrollo fullstack
* Implementar autenticación y seguridad real
* Aplicar buenas prácticas (DTOs, capas, validaciones)
* Simular un sistema de pedidos real

---

## 👨‍💻 Autor

**Mateo Calcagno**

---

## 📌 Estado del proyecto

🚧 En desarrollo 


---


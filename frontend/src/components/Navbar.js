import React from "react";
import { NavLink } from "react-router-dom";

function Navbar() {
  const routes = [
    { path: "products", label: "Productos" },
    { path: "orders", label: "Órdenes" },
    { path: "dashboard", label: "Panel" }
  ];

  return (
    <nav className="flex justify-center gap-10 bg-purple-900 p-4 rounded-xl shadow-lg text-white">
      {routes.map((route) => (
        <NavLink
          key={route.path}
          to={`/${route.path}`}
          className={({ isActive }) =>
            `px-5 py-2 rounded-xl font-semibold transition-all duration-300
            ${
              isActive
                ? "bg-gradient-to-r from-purple-300 to-purple-400 text-purple-900 scale-105"
                : "hover:bg-purple-600"
            }`
          }
        >
          {route.label}
        </NavLink>
      ))}
    </nav>
  );
}

export default Navbar;
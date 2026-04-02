import React from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Navbar() {
  const { logout, user } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  // Definimos qué links ve cada uno
  const links = [
    { to: "/products", label: "Productos", icon: "🏷️" },
    { to: "/orders", label: "Órdenes", icon: "📦" },
  ];

  // Solo añadimos el Panel si es ADMIN
  if (user?.role === "ADMIN") {
    links.push({ to: "/dashboard", label: "Panel Admin", icon: "📊" });
  }

  return (
    <nav className="flex items-center justify-between bg-white/80 backdrop-blur-md p-2 rounded-2xl shadow-lg border border-white/50">
      <div className="flex gap-2">
        {links.map((link) => (
          <NavLink
            key={link.to}
            to={link.to}
            className={({ isActive }) =>
              `flex items-center gap-2 px-6 py-2 rounded-xl font-bold transition-all ${
                isActive 
                  ? "bg-purple-600 text-white shadow-md scale-105" 
                  : "text-purple-900 hover:bg-purple-100"
              }`
            }
          >
            <span>{link.icon}</span>
            {link.label}
          </NavLink>
        ))}
      </div>

      <button 
        onClick={handleLogout}
        className="bg-red-100 text-red-600 hover:bg-red-600 hover:text-white px-6 py-2 rounded-xl font-bold transition-colors border border-red-200"
      >
        Cerrar Sesión
      </button>
    </nav>
  );
}

export default Navbar;
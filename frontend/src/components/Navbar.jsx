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

  const links = [
    { to: "/products", label: "Productos", icon: "🏷️" },
    { 
      to: "/orders", 
      label: user?.role === "ADMIN" ? "Órdenes" : "Mis Órdenes", 
      icon: "📦" 
    },
  ];

  if (user?.role === "ADMIN") {
    links.push({ to: "/dashboard", label: "Panel de Control", icon: "📊" });
  }

  return (
    <nav className="flex items-center justify-between bg-white/10 backdrop-blur-xl px-4 py-3 rounded-2xl border border-white/10 shadow-lg">

      {/* LINKS */}
      <div className="flex gap-2">
        {links.map((link) => (
          <NavLink
            key={link.to}
            to={link.to}
            className={({ isActive }) =>
              `flex items-center gap-2 px-5 py-2 rounded-xl text-sm font-semibold transition-all duration-200 ${
                isActive
                  ? "bg-gradient-to-r from-purple-600 to-pink-600 text-white shadow-md scale-105"
                  : "text-white/70 hover:text-white hover:bg-white/10"
              }`
            }
          >
            <span className="text-base">{link.icon}</span>
            {link.label}
          </NavLink>
        ))}
      </div>

      {/* USER + LOGOUT */}
      <div className="flex items-center gap-3">
        
        {/* USER NAME */}
        <span className="text-white/60 text-sm hidden sm:block">
          {user?.username}
        </span>

        {/* LOGOUT */}
        <button 
          onClick={handleLogout}
          className="bg-red-500/20 text-red-400 hover:bg-red-500 hover:text-white px-4 py-2 rounded-xl text-sm font-semibold transition-all duration-200"
        >
          Salir
        </button>
      </div>

    </nav>
  );
}

export default Navbar;
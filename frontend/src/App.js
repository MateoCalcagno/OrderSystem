import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Products from "./pages/Products";
import Orders from "./pages/Orders";
import Dashboard from "./pages/Dashboard";
import Navbar from "./components/Navbar";

function App() {
  return (
    <Router>
      <div className="h-screen w-screen bg-gradient-to-tr from-purple-100 via-pink-100 to-yellow-100 p-6 font-sans flex flex-col overflow-hidden relative">
        {/* Fondo decorativo */}
        <div className="absolute top-0 left-0 w-72 h-72 bg-purple-300 opacity-20 rounded-full blur-3xl"></div>
        <div className="absolute bottom-0 right-0 w-72 h-72 bg-pink-300 opacity-20 rounded-full blur-3xl"></div>
        {/* Header al costado izquierdo */}
        <header className="mb-6 pl-4">
          <div className="text-left">
            <h1 className="text-5xl font-extrabold text-purple-900 drop-shadow-lg tracking-tight">
              Sistema de Pedidos
            </h1>
            <p className="text-purple-900 mt-2 text-lg max-w-xl">
              Gestiona productos y órdenes de manera sencilla
            </p>
          </div>
        </header>

        <Navbar />

        {/* Main ocupa todo el espacio restante, más ancho y sin scroll */}
        <main className="w-full max-w-5xl mx-auto flex-1 mt-6 space-y-8 max-h-[calc(100vh-200px)]">
          <Routes>
            <Route path="/" element={<Navigate to="/products" />} />
            <Route path="/products" element={<Products />} />
            <Route path="/orders" element={<Orders />} />
            <Route path="/dashboard" element={<Dashboard />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
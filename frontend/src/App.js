import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider, useAuth } from "./context/AuthContext";
import Products from "./pages/Products";
import Orders from "./pages/Orders";
import Dashboard from "./pages/Dashboard";
import Login from "./pages/Login"; 
import Navbar from "./components/Navbar";
import SignUp from "./pages/SignUp";

// Componente para proteger rutas (Solo para logueados)
const PrivateRoute = ({ children }) => {
  const { user } = useAuth();
  return user ? children : <Navigate to="/login" />;
};

// Componente para proteger rutas de ADMIN
const AdminRoute = ({ children }) => {
  const { user } = useAuth();
  return user && user.role === "ADMIN" ? children : <Navigate to="/products" />;
};

function AppContent() {
  const { user } = useAuth();

  return (
    <Router>
      <div className="h-screen w-screen bg-gradient-to-br from-[#0f0f1a] via-[#1a1a2e] to-[#16213e] p-6 font-sans flex flex-col overflow-hidden relative text-white">
        {/* Fondo decorativo */}
        <div className="absolute top-0 left-0 w-72 h-72 bg-purple-300 opacity-20 rounded-full blur-3xl"></div>
        <div className="absolute bottom-0 right-0 w-72 h-72 bg-pink-300 opacity-20 rounded-full blur-3xl"></div>
        
        {/* Header (Restaurado al diseño original) */}
        <header className="mb-6 px-6 py-4 bg-white/10 backdrop-blur-xl rounded-2xl border border-white/10 shadow-lg">
          <div className="flex items-center justify-between">

            {/* TEXTO */}
            <div>
              <h1 className="text-4xl font-extrabold text-white tracking-tight">
                Sistema de Pedidos
              </h1>
              <p className="text-white/70 mt-1 text-sm">
                {user 
                  ? `Bienvenido, ${user.username} 👋`
                  : "Gestiona productos y órdenes de manera sencilla"}
              </p>
            </div>

            {/* LOGO */}
            <img
              src="/logoSP.jpeg"
              alt="Logo"
              className="w-14 h-14 object-contain opacity-90"
            />
            
          </div>
        </header>

        {/* Solo mostramos el Navbar si el usuario está logueado */}
        {user && <div className="mt-4"><Navbar /></div>}

        {/* Main Content */}
        <main className="w-full max-w-6xl mx-auto flex-1 mt-6 space-y-8 overflow-y-auto px-2">
          <Routes>
            {/* Ruta pública */}
            <Route path="/login" element={!user ? <Login /> : <Navigate to="/products" />} />
            <Route path="/signup" element={!user ? <SignUp /> : <Navigate to="/products" />} />

            {/* Rutas Protegidas */}
            <Route path="/products" element={<PrivateRoute><Products /></PrivateRoute>} />
            <Route path="/orders" element={<PrivateRoute><Orders /></PrivateRoute>} />
            <Route path="/dashboard" element={<AdminRoute><Dashboard /></AdminRoute>} />

            {/* Redirección por defecto */}
            <Route path="/" element={<Navigate to="/login" />} />
            <Route path="*" element={<Navigate to="/" />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
}

export default App;
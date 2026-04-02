import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider, useAuth } from "./context/AuthContext";
import Products from "./pages/Products";
import Orders from "./pages/Orders";
import Dashboard from "./pages/Dashboard";
import Login from "./pages/Login"; 
import Navbar from "./components/Navbar";

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
      <div className="h-screen w-screen bg-gradient-to-tr from-purple-100 via-pink-100 to-yellow-100 p-6 font-sans flex flex-col overflow-hidden relative">
        {/* Fondo decorativo */}
        <div className="absolute top-0 left-0 w-72 h-72 bg-purple-300 opacity-20 rounded-full blur-3xl"></div>
        <div className="absolute bottom-0 right-0 w-72 h-72 bg-pink-300 opacity-20 rounded-full blur-3xl"></div>
        
        {/* Header (Restaurado al diseño original) */}
        <header className="mb-6 pl-4 relative">
          <div className="text-left">
            <h1 className="text-5xl font-extrabold text-purple-900 drop-shadow-lg tracking-tight">
              Sistema de Pedidos
            </h1>
            <p className="text-purple-900 mt-2 text-lg max-w-xl font-medium">
              {user 
                ? `Bienvenido, ${user.username}` 
                : "Gestiona productos y órdenes de manera sencilla"}
            </p>
          </div>
          <img
            src="/logoSP.jpeg"
            alt="Logo"
            className="absolute top-0 right-9 w-20 h-20 object-contain rounded-xl shadow-sm"
          />
        </header>

        {/* Solo mostramos el Navbar si el usuario está logueado */}
        {user && <Navbar />}

        {/* Main Content */}
        <main className="w-full max-w-5xl mx-auto flex-1 mt-6 space-y-8 max-h-[calc(100vh-200px)] overflow-y-auto">
          <Routes>
            {/* Ruta pública */}
            <Route path="/login" element={!user ? <Login /> : <Navigate to="/products" />} />

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
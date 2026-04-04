import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider, useAuth } from "./context/AuthContext";
import Products from "./pages/Products";
import Orders from "./pages/Orders";
import Dashboard from "./pages/Dashboard";
import Login from "./pages/Login"; 
import Navbar from "./components/Navbar";
import SignUp from "./pages/SignUp";
import CartSidebar from "./components/CartSidebar";
import { Toaster } from "react-hot-toast";

function PrivateRoute({ children }) {
  const { user } = useAuth();
  return user ? children : <Navigate to="/login" />;
}

function AdminRoute({ children }) {
  const { user } = useAuth();
  return user && user.role === "ADMIN" ? children : <Navigate to="/products" />;
}

function AppContent() {
  const { user } = useAuth();

  return (
    <Router>
      {/* Configuracion de los Toasts */}
      <Toaster 
        position="bottom-right" 
        toastOptions={{
          style: { background: '#1f1f2e', color: '#fff', border: '1px solid rgba(255,255,255,0.1)' }
        }} 
      />

      <div className="h-screen w-screen bg-gradient-to-br from-[#0f0f1a] via-[#1a1a2e] to-[#16213e] p-6 font-sans flex flex-col overflow-hidden relative text-white">
        
        {/* Fondos decorativos originales */}
        <div className="absolute top-0 left-0 w-72 h-72 bg-purple-300 opacity-20 rounded-full blur-3xl pointer-events-none"></div>
        <div className="absolute bottom-0 right-0 w-72 h-72 bg-pink-300 opacity-20 rounded-full blur-3xl pointer-events-none"></div>
        
        {/* HEADER RESTAURADO */}
        <header className="mb-6 px-6 py-4 bg-white/10 backdrop-blur-xl rounded-2xl border border-white/10 shadow-lg">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-4xl font-extrabold text-white tracking-tight">Sistema de Pedidos</h1>
              <p className="text-white/70 mt-1 text-sm">
                {user ? `Bienvenido, ${user.username} 👋` : "Gestiona productos y órdenes de manera sencilla"}
              </p>
            </div>
            <img src="/logoSP.jpeg" alt="Logo" className="w-14 h-14 object-contain opacity-90" />
          </div>
        </header>

        {user && <div className="mb-6"><Navbar /></div>}

        {/* CONTENIDO DIVIDIDO */}
        <div className="flex flex-1 min-h-0 gap-6 overflow-hidden max-w-7xl mx-auto w-full">
          
          <main className="flex-1 overflow-y-auto pr-2 scrollbar-hide">
            <Routes>
              <Route path="/login" element={!user ? <Login /> : <Navigate to="/products" />} />
              <Route path="/signup" element={!user ? <SignUp /> : <Navigate to="/products" />} />
              <Route path="/products" element={<PrivateRoute><Products /></PrivateRoute>} />
              <Route path="/orders" element={<PrivateRoute><Orders /></PrivateRoute>} />
              <Route path="/dashboard" element={<AdminRoute><Dashboard /></AdminRoute>} />
              <Route path="/" element={<Navigate to="/login" />} />
              <Route path="*" element={<Navigate to="/" />} />
            </Routes>
          </main>

          {user && user.role === "USER" && (
            <div className="w-80 flex flex-col h-full">
              <CartSidebar />
            </div>
          )}
        </div>
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
import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import api from "../services/api";

function Orders() {
  const [orders, setOrders] = useState([]);
  const [products, setProducts] = useState([]);
  const [selectedProductId, setSelectedProductId] = useState("");
  const [cart, setCart] = useState([]);
  const [search, setSearch] = useState("");
  const { user } = useAuth();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [ordRes, prodRes] = await Promise.all([
          api.get("/orders"),
          api.get("/products"),
        ]);
        setOrders(ordRes.data);
        setProducts(prodRes.data);
      } catch (err) {
        console.error("Error al cargar datos", err);
      }
    };
    fetchData();
  }, []);

  const addToCart = () => {
    if (!selectedProductId) return;
    const product = products.find(p => p.id === parseInt(selectedProductId));
    if (product) {
      setCart([...cart, product]);
      setSelectedProductId("");
    }
  };

  const removeFromCart = (index) => {
    setCart(cart.filter((_, i) => i !== index));
  };

  const handleCreateOrder = async () => {
    if (cart.length === 0) return alert("El carrito está vacío");
    
    try {
      const productIds = cart.map(p => p.id);
      const res = await api.post("/orders", { productIds });
      
      setOrders([...orders, res.data]);
      setCart([]);
      alert("¡Pedido realizado con éxito!");
    } catch (err) {
      alert("Error al crear el pedido");
    }
  };

  // 🔍 FILTRO
  const filteredOrders = orders.filter(o =>
    o.username?.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="bg-white/10 backdrop-blur-xl p-6 rounded-2xl shadow-lg border border-white/10 flex flex-col h-full max-w-5xl mx-auto">

      {/* HEADER */}
      <div className="mb-6">
        <h2 className="text-2xl font-bold text-white">
          {user.role === "ADMIN" ? "📦 Gestión de Órdenes" : "🛍️ Mis Compras"}
        </h2>
      </div>

      {/* CREAR ORDEN */}
      {user.role === "USER" && (
        <div className="mb-6 space-y-4">

          <div className="flex gap-3 bg-white/5 p-4 rounded-2xl border border-white/10">

            {/* 🔥 SELECT ARREGLADO */}
            <select
              className="flex-1 p-3 rounded-xl bg-white text-gray-900 border border-white/20 outline-none"
              value={selectedProductId}
              onChange={(e) => setSelectedProductId(e.target.value)}
            >
              <option value="">Selecciona un producto...</option>
              {products.map((p) => (
                <option key={p.id} value={p.id}>
                  {p.name}
                </option>
              ))}
            </select>

            <button
              onClick={addToCart}
              className="bg-gradient-to-r from-purple-600 to-pink-600 px-6 py-3 rounded-xl font-bold text-white hover:scale-105 active:scale-95 transition-all shadow-lg"
            >
              + Agregar
            </button>
          </div>

          {cart.length > 0 && (
            <div className="bg-white/5 p-5 rounded-2xl border border-white/10">
              <p className="text-white/70 text-sm mb-3">Tu pedido:</p>

              <div className="flex flex-wrap gap-2 mb-4">
                {cart.map((p, index) => (
                  <span 
                    key={index} 
                    className="bg-white/10 border border-white/20 px-3 py-1 rounded-full text-sm text-white flex items-center gap-2"
                  >
                    {p.name}
                    <button 
                      onClick={() => removeFromCart(index)} 
                      className="text-red-400 hover:text-red-500"
                    >
                      ✕
                    </button>
                  </span>
                ))}
              </div>

              <button
                onClick={handleCreateOrder}
                className="w-full bg-gradient-to-r from-purple-600 to-pink-600 py-3 rounded-xl font-bold text-white hover:scale-[1.02] active:scale-95 transition-all shadow-lg"
              >
                Confirmar Compra ({cart.length})
              </button>
            </div>
          )}
        </div>
      )}

      {/* HISTORIAL (FIJO) */}
      <div className="mb-4">
        <h3 className="text-sm text-white/50 font-semibold uppercase tracking-wider mb-3">
          Historial
        </h3>

        {user.role === "ADMIN" && (
          <input
            type="text"
            placeholder="Buscar por cliente..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full p-3 rounded-xl bg-white/10 border border-white/20 text-white placeholder:text-white/40 outline-none focus:ring-2 focus:ring-purple-500"
          />
        )}
      </div>

      {/* 📦 LISTA CON SCROLL */}
      <div className="flex-1 overflow-y-auto pr-2">
        <div className="space-y-4">

          {filteredOrders.map((o) => (
            <div 
              key={o.id} 
              className="bg-white/5 p-4 rounded-2xl border border-white/10 flex justify-between items-center hover:scale-[1.01] transition-all"
            >
              
              <div className="flex items-center gap-4">

                <div className="w-10 h-10 bg-purple-600/20 rounded-lg flex items-center justify-center text-white text-sm font-bold">
                  #{o.id}
                </div>

                <div>
                  <p className="text-white font-semibold">
                    {Array.isArray(o.products) ? o.products.join(", ") : "Sin productos"}
                  </p>

                  {user.role === "ADMIN" && (
                    <p className="text-xs text-pink-400">
                      Cliente: {o.username}
                    </p>
                  )}
                </div>
              </div>

              <span className="text-xs bg-green-500/20 text-green-400 px-3 py-1 rounded-full border border-green-500/20 font-semibold">
                COMPLETADO
              </span>
            </div>
          ))}

        </div>
      </div>

    </div>
  );
}

export default Orders;
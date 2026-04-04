import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import api from "../services/api";

function Orders() {
  const [orders, setOrders] = useState([]);
  const [search, setSearch] = useState("");
  const { user } = useAuth();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await api.get("/orders");
        setOrders(res.data);
      } catch (err) { console.error("Error"); }
    };
    fetchData();
  }, []);

  // --- FUNCIÓN PARA AGRUPAR PRODUCTOS ---
  const formatProducts = (productsArray) => {
    if (!Array.isArray(productsArray) || productsArray.length === 0) return "Sin productos";

    // Contamos las ocurrencias: { "Coca Cola": 2, "Fernet": 1 }
    const counts = productsArray.reduce((acc, name) => {
      acc[name] = (acc[name] || 0) + 1;
      return acc;
    }, {});

    // Convertimos el objeto a un string: "Coca Cola (2), Fernet (1)"
    return Object.entries(counts)
      .map(([name, count]) => `${name} (${count})`)
      .join(", ");
  };

  const filteredOrders = orders.filter(o => 
    o.username?.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="bg-white/10 backdrop-blur-xl p-6 rounded-2xl border border-white/10 h-full flex flex-col shadow-lg">
      <h2 className="text-2xl font-bold text-white mb-6 flex items-center gap-2">
        {user.role === "ADMIN" ? "📦 Gestión de Órdenes" : "🛍️ Mis Compras"}
      </h2>

      {user.role === "ADMIN" && (
        <input 
          type="text" 
          placeholder="Buscar cliente..." 
          value={search} 
          onChange={(e) => setSearch(e.target.value)} 
          className="w-full p-3 rounded-xl bg-white/5 border border-white/20 text-white mb-4 outline-none focus:ring-2 focus:ring-purple-500 transition-all" 
        />
      )}

      <div className="flex-1 overflow-y-auto space-y-3 pr-2 scrollbar-hide">
        {filteredOrders.length === 0 ? (
          <p className="text-white/30 italic text-center py-10">No hay órdenes registradas</p>
        ) : (
          filteredOrders.map((o) => (
            <div key={o.id} className="bg-white/5 p-4 rounded-xl border border-white/5 flex justify-between items-center text-white hover:bg-white/10 transition-all group">
              <div className="flex items-center gap-4">
                <div className="w-10 h-10 bg-purple-600/20 rounded-lg flex items-center justify-center font-bold text-xs text-purple-400 border border-purple-500/20 shadow-inner">
                  #{o.id}
                </div>
                <div>
                  {/* AQUÍ USAMOS LA FUNCIÓN DE FORMATO */}
                  <p className="font-semibold text-sm text-white/90">
                    {formatProducts(o.products)}
                  </p>
                  {user.role === "ADMIN" && (
                    <p className="text-[10px] text-pink-400 uppercase tracking-widest mt-1">
                      Cliente: {o.username}
                    </p>
                  )}
                </div>
              </div>
              <span className="text-[10px] bg-green-500/20 text-green-400 px-3 py-1 rounded-full border border-green-500/20 font-bold uppercase tracking-tighter">
                Completado
              </span>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default Orders;
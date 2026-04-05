import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import api from "../services/api";
import { FaTrash } from "react-icons/fa";
import toast from "react-hot-toast";

function Orders() {
  const [orders, setOrders] = useState([]);
  const [search, setSearch] = useState("");
  const { user } = useAuth();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await api.get("/orders");
        setOrders(res.data);
      } catch (err) { 
        console.error("Error al cargar órdenes", err); 
      }
    };
    fetchData();
  }, []);

  const formatProducts = (productsArray) => {
    if (!Array.isArray(productsArray) || productsArray.length === 0) return "Sin productos";
    const counts = productsArray.reduce((acc, name) => {
      acc[name] = (acc[name] || 0) + 1;
      return acc;
    }, {});
    return Object.entries(counts)
      .map(([name, count]) => `${name} (${count})`)
      .join(", ");
  };

  const filteredOrders = orders.filter(o => 
    o.username?.toLowerCase().includes(search.toLowerCase())
  );

  const handleDelete = async (id) => {
    try {
      await api.delete(`/orders/${id}`);
      setOrders(prev => prev.filter(o => o.id !== id));
      toast.success("Orden eliminada", {
        style: {
          borderRadius: '12px',
          background: '#18181b',
          color: '#fff',
          border: '1px solid rgba(255,255,255,0.1)',
        },
      });
    } catch (err) {
      console.error("Error al eliminar", err);
      toast.error("No se pudo eliminar");
    }
  };

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
          className="w-full p-3 rounded-xl bg-white/5 border border-white/20 text-white mb-6 outline-none focus:ring-2 focus:ring-purple-500/50 transition-all placeholder:text-white/20" 
        />
      )}

      {/* Contenedor de la lista o mensaje de vacío */}
      <div className="flex-1 overflow-y-auto space-y-4 pr-2 scrollbar-hide flex flex-col">
        {filteredOrders.length === 0 ? (
          // Este div ocupa todo el alto y centra el contenido
          <div className="flex-1 flex items-center justify-center">
            <p className="text-white/30 text-base italic tracking-wide">
              No hay órdenes registradas
            </p>
          </div>
        ) : (
          filteredOrders.map((o) => (
            <div
              key={o.id}
              className="bg-white/5 p-4 px-6 rounded-2xl border border-white/5 grid grid-cols-3 items-center gap-4 text-white hover:bg-white/10 transition-all group"
            >
              {/* Sección 1: Info Principal */}
              <div className="flex items-center gap-4 min-w-0">
                <div className="w-11 h-11 flex-shrink-0 bg-purple-600/20 rounded-xl flex items-center justify-center font-bold text-xs text-purple-400 border border-purple-500/10 shadow-lg">
                  #{o.id}
                </div>
                <div className="truncate">
                  <p className="font-semibold text-base text-white/90 truncate">
                    {formatProducts(o.products)}
                  </p>
                  {user.role === "ADMIN" && (
                    <p className="text-[11px] text-pink-400/80 uppercase tracking-widest mt-0.5">
                      {o.username}
                    </p>
                  )}
                </div>
              </div>

              {/* Sección 2: Fecha (Centro) */}
              <div className="text-center">
                <p className="text-[12px] text-white/30 font-medium tracking-wide">
                  {new Date(o.createdAt).toLocaleString([], { 
                    day: '2-digit', 
                    month: '2-digit', 
                    hour: '2-digit', 
                    minute: '2-digit' 
                  })}
                </p>
              </div>

              {/* Sección 3: Acciones */}
              <div className="flex justify-end">
                {(user.role === "ADMIN" || o.username === user.username) && (
                  <button
                    onClick={() => handleDelete(o.id)}
                    className="w-10 h-10 flex items-center justify-center text-white/10 hover:text-red-500 hover:bg-red-500/10 rounded-xl transition-all group-hover:text-red-400/60"
                  >
                    <FaTrash size={15} />
                  </button>
                )}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default Orders;
import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import api from "../services/api";

function Orders() {
  const [orders, setOrders] = useState([]);
  const [products, setProducts] = useState([]);
  const [selectedProduct, setSelectedProduct] = useState("");
  const { user } = useAuth();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [ordRes, prodRes] = await Promise.all([api.get("/orders"), api.get("/products")]);
        setOrders(ordRes.data);
        setProducts(prodRes.data);
      } catch (err) {
        console.error("Error al cargar datos", err);
      }
    };
    fetchData();
  }, []);

  const handleCreateOrder = async () => {
    if (!selectedProduct) return alert("Selecciona un producto");
    try {
      const res = await api.post("/orders", { productIds: [parseInt(selectedProduct)] });
      setOrders([...orders, res.data]);
      setSelectedProduct("");
    } catch (err) {
      alert("Error al crear el pedido");
    }
  };

  return (
    <div className="bg-white/70 p-6 rounded-2xl shadow-lg border border-purple-200 h-full flex flex-col">
      <h2 className="text-2xl font-bold mb-4 text-purple-900">
        {user.role === "ADMIN" ? "📦 Todas las Órdenes" : "🛍️ Mis Compras"}
      </h2>

      {user.role === "USER" && (
        <div className="flex gap-2 mb-6 bg-purple-50 p-3 rounded-xl border border-purple-100">
          <select 
            className="flex-1 p-2 rounded-lg border border-purple-300 outline-none"
            value={selectedProduct}
            onChange={(e) => setSelectedProduct(e.target.value)}
          >
            <option value="">¿Qué vas a comprar hoy?</option>
            {products.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
          </select>
          <button onClick={handleCreateOrder} className="bg-purple-600 text-white px-6 py-2 rounded-lg font-bold hover:bg-purple-700">Comprar</button>
        </div>
      )}

      <div className="space-y-3 overflow-y-auto pr-2">
        {orders.map(o => (
          <div key={o.id} className="bg-white p-4 rounded-xl border-l-4 border-purple-500 shadow-sm flex justify-between">
            <div>
              <p className="text-xs font-bold text-purple-400">TICKET #{o.id}</p>
              <p className="text-purple-900 font-semibold">{o.products.join(", ")}</p>
              {user.role === "ADMIN" && <p className="text-xs text-pink-500 italic">Cliente: {o.username}</p>}
            </div>
            <span className="text-green-600 font-bold text-sm">✓ RECIBIDO</span>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Orders;
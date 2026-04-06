import React, { useEffect, useState } from "react";
import api from "../services/api";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from "recharts";

function Dashboard() {
  const [data, setData] = useState({ orders: [], products: [] });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        const [ordersRes, productsRes] = await Promise.all([
          api.get("/orders"),
          api.get("/products")
        ]);
        setData({ orders: ordersRes.data, products: productsRes.data });
      } catch (err) {
        console.error("Error cargando dashboard", err);
      } finally {
        setLoading(false);
      }
    };
    loadDashboard();
  }, []);

  const productCount = {};
  data.orders.forEach(order => {
    order.products.forEach(productName => {
      productCount[productName] = (productCount[productName] || 0) + 1;
    });
  });

  const chartData = Object.entries(productCount).map(([name, count]) => ({ name, count }));

  if (loading) {
    return (
      <div className="flex items-center justify-center h-full text-white/60 animate-pulse">
        Cargando estadísticas...
      </div>
    );
  }

  return (
    <div className="bg-white/10 backdrop-blur-xl p-6 rounded-2xl shadow-lg border border-white/10 h-full flex flex-col">

      {/* TÍTULO */}
      <h2 className="text-2xl font-bold mb-6 text-white">
        📊 Análisis de Ventas
      </h2>

      {/* MÉTRICAS */}
      <div className="grid grid-cols-2 gap-4 mb-6">
        
        <div className="bg-gradient-to-r from-purple-600 to-pink-600 p-5 rounded-2xl text-white shadow-lg hover:scale-[1.02] transition-all">
          <p className="text-sm opacity-80">Total Órdenes</p>
          <p className="text-3xl font-bold">{data.orders.length}</p>
        </div>

        <div className="bg-gradient-to-r from-pink-500 to-orange-400 p-5 rounded-2xl text-white shadow-lg hover:scale-[1.02] transition-all">
          <p className="text-sm opacity-80">Productos Activos</p>
          <p className="text-3xl font-bold">{data.products.length}</p>
        </div>

      </div>

      {/* GRÁFICO (se adapta al espacio restante) */}
      <div className="flex-1 bg-white/5 p-4 rounded-2xl border border-white/10">
        <ResponsiveContainer width="100%" height="100%">
          <BarChart data={chartData}>
            
            <defs>
              <linearGradient id="colorGradient" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stopColor="#a855f7" />
                <stop offset="100%" stopColor="#ec4899" />
              </linearGradient>
            </defs>

            <XAxis dataKey="name" stroke="#ccc" />
            <YAxis stroke="#ccc" />
            <Tooltip
              contentStyle={{
                backgroundColor: "#1f1f1f",
                border: "none",
                borderRadius: "12px",
                color: "white",
                boxShadow: "0 10px 30px rgba(0,0,0,0.5)"
              }}
              labelStyle={{ color: "#ccc" }}
              itemStyle={{ color: "#fff" }}
              formatter={(value) => [`${value}`, "Cantidad"]}
            />
            <Bar 
              dataKey="count" 
              fill="url(#colorGradient)" 
              radius={[6, 6, 0, 0]} 
            />

          </BarChart>
        </ResponsiveContainer>
      </div>

    </div>
  );
}

export default Dashboard;
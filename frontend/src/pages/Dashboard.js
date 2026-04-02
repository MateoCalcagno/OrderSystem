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

  // Contar frecuencia de productos en las órdenes
  const productCount = {};
  data.orders.forEach(order => {
    order.products.forEach(productName => {
      productCount[productName] = (productCount[productName] || 0) + 1;
    });
  });

  const chartData = Object.entries(productCount).map(([name, count]) => ({ name, count }));

  if (loading) return <div className="p-10 text-purple-900 font-bold">Cargando estadísticas...</div>;

  return (
    <div className="bg-white/80 p-6 rounded-2xl shadow-xl border border-purple-200">
      <h2 className="text-2xl font-bold mb-6 text-purple-900">📊 Análisis de Ventas</h2>
      <div className="grid grid-cols-2 gap-4 mb-6">
        <div className="bg-purple-600 p-4 rounded-xl text-white shadow-lg">
          <p className="text-sm opacity-80">Total Órdenes</p>
          <p className="text-3xl font-bold">{data.orders.length}</p>
        </div>
        <div className="bg-pink-500 p-4 rounded-xl text-white shadow-lg">
          <p className="text-sm opacity-80">Productos Activos</p>
          <p className="text-3xl font-bold">{data.products.length}</p>
        </div>
      </div>
      <div className="h-64 bg-white p-2 rounded-xl border border-purple-100">
        <ResponsiveContainer width="100%" height="100%">
          <BarChart data={chartData}>
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Bar dataKey="count" fill="#9333ea" radius={[4, 4, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}

export default Dashboard;
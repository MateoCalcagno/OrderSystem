import React, { useEffect, useState } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer
} from "recharts";

function Dashboard() {
  const [orders, setOrders] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const ordersRes = await fetch("http://localhost:8080/orders");
      const productsRes = await fetch("http://localhost:8080/products");

      if (!ordersRes.ok || !productsRes.ok) {
        throw new Error("Error al cargar datos");
      }

      const ordersData = await ordersRes.json();
      const productsData = await productsRes.json();

      setOrders(ordersData);
      setProducts(productsData);
    } catch (err) {
      console.error(err);
      setError("No se pudieron cargar los datos");
    } finally {
      setLoading(false);
    }
  };

  // 🧠 Contar productos de forma segura
  const productCount = {};
  orders.forEach(order => {
    if (order.products) {
      order.products.forEach(p => {
        productCount[p] = (productCount[p] || 0) + 1;
      });
    }
  });

  const chartData = Object.entries(productCount).map(([name, count]) => ({
    name,
    count
  }));

  // ⏳ Loading
  if (loading) {
    return <p className="text-white">Cargando datos...</p>;
  }

  // ❌ Error
  if (error) {
    return <p className="text-red-400">{error}</p>;
  }

  return (
    <div className="bg-gradient-to-r from-purple-100 via-purple-200 to-purple-300 rounded-2xl p-6 border border-purple-300 text-purple-900">
      
      <h2 className="text-2xl font-bold mb-6">📊 Panel de Control</h2>

      <p className="text-lg mb-2">
        Total de productos: <strong>{products.length}</strong>
      </p>
      <p className="text-lg mb-4">
        Total de órdenes: <strong>{orders.length}</strong>
      </p>

      {/* 📊 GRÁFICO */}
      <div className="bg-white rounded-xl p-4 h-64">
        <ResponsiveContainer width="100%" height="100%">
          <BarChart data={chartData}>
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Bar dataKey="count" />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}

export default Dashboard;
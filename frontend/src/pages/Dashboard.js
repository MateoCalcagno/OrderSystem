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

  useEffect(() => {
    fetch("http://localhost:8080/orders")
      .then(res => res.json())
      .then(data => setOrders(data))
      .catch(err => console.error(err));

    fetch("http://localhost:8080/products")
      .then(res => res.json())
      .then(data => setProducts(data))
      .catch(err => console.error(err));
  }, []);

  // contar productos
  const productCount = {};
  orders.forEach(order => {
    order.products.forEach(p => {
      productCount[p] = (productCount[p] || 0) + 1;
    });
  });

  // convertir a formato gráfico
  const chartData = Object.entries(productCount).map(([name, count]) => ({
    name,
    count
  }));

  return (
    <div className="bg-gradient-to-r from-purple-100 via-purple-200 to-purple-300 rounded-2xl p-6 border border-purple-300 text-purple-900">
      
      <h2 className="text-3xl font-bold mb-6">📊 Panel de Control</h2>

      <p className="text-lg mb-2">
        Total de productos: <strong>{products.length}</strong>
      </p>
      <p className="text-lg mb-4">
        Total de órdenes: <strong>{orders.length}</strong>
      </p>

      {/* 🔥 GRÁFICO */}
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
import React from "react";
import { useDashboard } from "../hooks/useDashboard";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from "recharts";

function Dashboard() {
  const { orders, products, chartData, loading } = useDashboard();

  if (loading) {
    return (
      <div className="flex items-center justify-center h-full text-white/60 animate-pulse">
        Cargando estadísticas...
      </div>
    );
  }

  return (
    <div className="bg-white/10 backdrop-blur-xl p-6 rounded-2xl shadow-lg border border-white/10 h-full flex flex-col">

      <h2 className="text-2xl font-bold mb-6 text-white">
        📊 Análisis de Ventas
      </h2>

      {/* MÉTRICAS */}
      <div className="grid grid-cols-2 gap-4 mb-6">
        
        <div className="bg-gradient-to-r from-purple-600 to-pink-600 p-5 rounded-2xl text-white shadow-lg">
          <p className="text-sm opacity-80">Total Órdenes</p>
          <p className="text-3xl font-bold">{orders.length}</p>
        </div>

        <div className="bg-gradient-to-r from-pink-500 to-orange-400 p-5 rounded-2xl text-white shadow-lg">
          <p className="text-sm opacity-80">Productos Activos</p>
          <p className="text-3xl font-bold">{products.length}</p>
        </div>

      </div>

      {/* GRÁFICO */}
      <div className="flex-1 bg-white/5 p-4 rounded-2xl border border-white/10">
        <ResponsiveContainer width="100%" height="100%">
          <BarChart data={chartData}>
            <XAxis dataKey="name" stroke="#ccc" />
            <YAxis stroke="#ccc" />
            <Tooltip />
            <Bar dataKey="count" fill="#a855f7" radius={[6, 6, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </div>

    </div>
  );
}

export default Dashboard;
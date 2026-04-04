import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import api from "../services/api";

function Products() {
  const [products, setProducts] = useState([]);
  const [newProduct, setNewProduct] = useState("");
  const [search, setSearch] = useState("");
  const { user } = useAuth();

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    try {
      const res = await api.get("/products");
      setProducts(res.data);
    } catch (err) {
      console.error("Error al cargar productos");
    }
  };

  const handleAddProduct = async () => {
    if (!newProduct.trim()) return;
    try {
      const res = await api.post("/products", { name: newProduct });
      setProducts([...products, res.data]);
      setNewProduct("");
    } catch (err) {
      alert("Error al agregar: Solo Admins");
    }
  };

  const handleDelete = async (id) => {
    try {
      await api.delete(`/products/${id}`);
      setProducts(products.filter(p => p.id !== id));
    } catch (err) {
      alert("No tienes permiso para borrar");
    }
  };

  // 🔍 FILTRO
  const filteredProducts = products.filter(p =>
    p.name.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="bg-white/10 backdrop-blur-xl p-6 rounded-2xl shadow-lg border border-white/10 h-full flex flex-col">

      <h2 className="text-2xl font-bold mb-6 text-white">🏷️ Productos</h2>

      {/* 🔍 BUSCADOR */}
      <div className="mb-4">
        <input
          type="text"
          placeholder="Buscar producto..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="w-full p-3 rounded-xl bg-white/10 border border-white/20 text-white placeholder:text-white/40 outline-none focus:ring-2 focus:ring-purple-500"
        />
      </div>

      {/* ➕ AGREGAR PRODUCTO */}
      {user.role === "ADMIN" && (
        <div className="flex gap-3 mb-6">
          <input 
            className="flex-1 p-3 rounded-xl bg-white/10 border border-white/20 text-white placeholder:text-white/40 outline-none focus:ring-2 focus:ring-purple-500"
            value={newProduct}
            onChange={(e) => setNewProduct(e.target.value)}
            placeholder="Nuevo producto..."
          />
          <button 
            onClick={handleAddProduct} 
            className="bg-gradient-to-r from-purple-600 to-pink-600 px-6 py-3 rounded-xl font-bold text-white hover:scale-105 active:scale-95 transition-all shadow-lg"
          >
            Añadir
          </button>
        </div>
      )}

      {/* 📦 LISTA CON SCROLL */}
      <div className="flex-1 overflow-y-auto pr-2">
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">

          {filteredProducts.map(p => (
            <div 
              key={p.id} 
              className="bg-white/5 backdrop-blur-lg p-4 rounded-xl border border-white/10 shadow-md hover:shadow-xl hover:scale-[1.02] transition-all duration-200 flex justify-between items-center"
            >
              
              <span className="text-white font-semibold">
                {p.name}
              </span>

              {user.role === "ADMIN" && (
                <button 
                  onClick={() => handleDelete(p.id)} 
                  className="text-sm bg-red-500/20 text-red-400 hover:bg-red-500 hover:text-white px-3 py-1 rounded-lg transition-all"
                >
                  Eliminar
                </button>
              )}

            </div>
          ))}

        </div>
      </div>

    </div>
  );
}

export default Products;
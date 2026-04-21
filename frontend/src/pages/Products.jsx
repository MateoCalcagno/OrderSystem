import React, { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { useCart } from "../context/CartContext";
import { useProducts } from "../hooks/useProducts";
import toast from "react-hot-toast";
import { FaTrash } from "react-icons/fa";

function Products() {
  const { products, createProduct, deleteProduct, currentPage, totalPages, loadProducts } = useProducts();

  const [newProduct, setNewProduct] = useState({ name: "", price: "" });
  const [search, setSearch] = useState("");

  const { user } = useAuth();
  const { addToCart } = useCart();

  const filteredProducts = products.filter(p =>
    p.name?.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="bg-white/10 backdrop-blur-xl p-6 rounded-2xl border border-white/10 h-full flex flex-col shadow-lg">
      
      <h2 className="text-2xl font-bold mb-6 text-white flex items-center gap-2">
        🏷️ Productos
      </h2>

      {/* 🔍 BUSCADOR */}
      <input 
        type="text"
        placeholder="Buscar producto..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        className="w-full p-3 rounded-xl bg-white/5 border border-white/20 text-white mb-6 outline-none focus:ring-2 focus:ring-purple-500 transition-all"
      />

      {/* ➕ CREAR PRODUCTO (ADMIN) */}
      {user.role === "ADMIN" && (
        <div className="flex gap-3 mb-6">
          <input
            className="flex-1 p-3 rounded-xl bg-white/5 border border-white/20 text-white"
            value={newProduct.name}
            onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
            placeholder="Nombre del producto..."
          />
          <input
            className="w-28 p-3 rounded-xl bg-white/5 border border-white/20 text-white"
            value={newProduct.price}
            onChange={(e) => setNewProduct({ ...newProduct, price: e.target.value })}
            placeholder="$..."
            type="number"
            min="0.01"
            step="0.01"
          />
          <button
            onClick={() => {
              createProduct(newProduct.name, newProduct.price);
              setNewProduct({ name: "", price: "" });
            }}
            className="bg-purple-600 px-6 py-3 rounded-xl font-bold hover:bg-purple-700 transition-all shadow-lg"
          >
            Añadir
          </button>
        </div>
      )}

      {/* 📦 LISTA */}
      <div className="flex-1 overflow-y-auto pr-2 scrollbar-hide">
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-2 gap-4">
          
          {filteredProducts.map(p => (
            <div
              key={p.id}
              className="bg-white/5 p-4 rounded-xl border border-white/5 flex justify-between items-center hover:bg-white/10 transition-all group"
            >
              <div className="flex flex-col">
                <span className="text-white font-medium">{p.name}</span>
                <span className="text-green-400/70 text-xs font-bold">${p.price?.toFixed(2)}</span>
              </div>

              <div className="flex items-center gap-2">

                {/* 🛒 USER */}
                {user.role === "USER" && (
                  <button 
                    onClick={() => {
                      addToCart(p);
                      toast.success(`${p.name} añadido`, { icon: '🛒', duration: 1000 });
                    }} 
                    className="bg-green-500/20 text-green-400 hover:bg-green-500 hover:text-white px-3 py-1 rounded-lg text-xs font-bold transition-all"
                  >
                    + Carrito
                  </button>
                )}

                {/* 🗑️ ADMIN */}
                {user.role === "ADMIN" && (
                  <button
                    onClick={() => deleteProduct(p.id)}
                    className="text-white/20 hover:text-red-500 transition-all"
                  >
                    <FaTrash size={13} />
                  </button>
                )}

              </div>
            </div>
          ))}

        </div>
      </div>
      {totalPages > 1 && (
        <div className="flex justify-center items-center gap-3 mt-4 pt-4 border-t border-white/10">
          <button
            onClick={() => loadProducts(currentPage - 1)}
            disabled={currentPage === 0}
            className="px-4 py-2 rounded-xl bg-white/5 border border-white/10 text-white/50 hover:bg-white/10 disabled:opacity-20 disabled:cursor-not-allowed transition-all"
          >
            ←
          </button>
          <span className="text-white/40 text-xs">
            {currentPage + 1} / {totalPages}
          </span>
          <button
            onClick={() => loadProducts(currentPage + 1)}
            disabled={currentPage === totalPages - 1}
            className="px-4 py-2 rounded-xl bg-white/5 border border-white/10 text-white/50 hover:bg-white/10 disabled:opacity-20 disabled:cursor-not-allowed transition-all"
          >
            →
          </button>
        </div>
      )}
    </div>
  );
}

export default Products;
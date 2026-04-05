import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import api from "../services/api";
import toast from "react-hot-toast";
import { FaTrash } from "react-icons/fa";

function Products() {
  const [products, setProducts] = useState([]);
  const [newProduct, setNewProduct] = useState("");
  const [search, setSearch] = useState("");
  const { user, addToCart } = useAuth();

  useEffect(() => { loadProducts(); }, []);

  const loadProducts = async () => {
    try {
      const res = await api.get("/products");
      setProducts(res.data);
    } catch (err) { toast.error("Error al cargar productos"); }
  };

  const handleAddProduct = async () => {
    if (!newProduct.trim()) return;
    try {
      const res = await api.post("/products", { name: newProduct });
      setProducts([...products, res.data]);
      setNewProduct("");
      toast.success("Producto creado 🏷️");
    } catch (err) { toast.error("Solo administradores"); }
  };

  const handleDeleteProduct = async (id) => {
    try {
      await api.delete(`/products/${id}`);
      setProducts(prev => prev.filter(p => p.id !== id));
      toast.success("Eliminado");
    } catch (err) { toast.error("Error al borrar"); }
  };

  const filteredProducts = products.filter(p => p.name.toLowerCase().includes(search.toLowerCase()));

  return (
    <div className="bg-white/10 backdrop-blur-xl p-6 rounded-2xl border border-white/10 h-full flex flex-col shadow-lg">
      <h2 className="text-2xl font-bold mb-6 text-white flex items-center gap-2">🏷️ Productos</h2>
      
      <input 
        type="text" placeholder="Buscar producto..." value={search}
        onChange={(e) => setSearch(e.target.value)}
        className="w-full p-3 rounded-xl bg-white/5 border border-white/20 text-white mb-6 outline-none focus:ring-2 focus:ring-purple-500 transition-all"
      />

      {user.role === "ADMIN" && (
        <div className="flex gap-3 mb-6">
          <input className="flex-1 p-3 rounded-xl bg-white/5 border border-white/20 text-white" value={newProduct} onChange={(e) => setNewProduct(e.target.value)} placeholder="Nuevo producto..." />
          <button onClick={handleAddProduct} className="bg-purple-600 px-6 py-3 rounded-xl font-bold hover:bg-purple-700 transition-all shadow-lg">Añadir</button>
        </div>
      )}

      <div className="flex-1 overflow-y-auto pr-2 scrollbar-hide">
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-2 gap-4">
          {filteredProducts.map(p => (
            <div key={p.id} className="bg-white/5 p-4 rounded-xl border border-white/5 flex justify-between items-center hover:bg-white/10 transition-all group">
              <span className="text-white font-medium">{p.name}</span>
              
              <div className="flex items-center gap-2">
                {user.role === "USER" && (
                  <button 
                    onClick={() => {
                      addToCart(p);
                      toast.success(`${p.name} añadido`, { icon: '🛒', duration: 1000 });
                    }} 
                    className="bg-green-500/20 text-green-400 hover:bg-green-500 hover:text-white px-3 py-1 rounded-lg text-xs font-bold transition-all"
                  >+ Carrito</button>
                )}

                {user.role === "ADMIN" && (
                  <button onClick={() => handleDeleteProduct(p.id)} className="text-white/20 hover:text-red-500 transition-all">
                    <FaTrash size={13} />
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Products;
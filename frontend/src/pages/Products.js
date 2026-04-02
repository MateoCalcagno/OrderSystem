import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import api from "../services/api";

function Products() {
  const [products, setProducts] = useState([]);
  const [newProduct, setNewProduct] = useState("");
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

  return (
    <div className="bg-white/70 p-6 rounded-2xl shadow-lg border border-purple-200">
      <h2 className="text-2xl font-bold mb-4 text-purple-900">🏷️ Productos</h2>
      
      {user.role === "ADMIN" && (
        <div className="flex gap-2 mb-4">
          <input 
            className="flex-1 p-2 rounded-lg border border-purple-300"
            value={newProduct}
            onChange={(e) => setNewProduct(e.target.value)}
            placeholder="Nuevo producto..."
          />
          <button onClick={handleAddProduct} className="bg-purple-600 text-white px-4 py-2 rounded-lg">Añadir</button>
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {products.map(p => (
          <div key={p.id} className="bg-white p-4 rounded-xl shadow flex justify-between items-center border border-purple-100">
            <span className="font-semibold text-purple-800">{p.name}</span>
            {user.role === "ADMIN" && (
              <button onClick={() => handleDelete(p.id)} className="text-red-500 font-bold">✕</button>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}

export default Products;
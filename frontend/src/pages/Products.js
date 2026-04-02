import React, { useEffect, useState } from "react";

function Products() {
  const [products, setProducts] = useState([]);
  const [search, setSearch] = useState("");
  const [newProduct, setNewProduct] = useState("");

  useEffect(() => {
    fetch("http://localhost:8080/products")
      .then(res => res.json())
      .then(data => setProducts(data))
      .catch(err => console.error(err));
  }, []);

  const handleAddProduct = () => {
    if (!newProduct.trim()) return;
    fetch("http://localhost:8080/products", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: newProduct })
    })
      .then(res => res.json())
      .then(data => {
        setProducts([...products, data]);
        setNewProduct("");
      })
      .catch(err => console.error(err));
  };

  const handleDelete = (id) => {
    fetch(`http://localhost:8080/products/${id}`, { method: "DELETE" })
      .then(() => setProducts(products.filter(p => p.id !== id)))
      .catch(err => console.error(err));
  };

  const filteredProducts = products.filter(p =>
    p.name.toLowerCase().startsWith(search.toLowerCase())
  );

  return (
    <div className="bg-gradient-to-r from-purple-100 via-purple-200 to-purple-300 shadow-lg rounded-2xl p-6 border border-purple-300 text-purple-900 flex flex-col max-h-[450px]">
      
      <h2 className="text-2xl font-bold mb-4">🏷️ Productos</h2>

      <input
        type="text"
        placeholder="Buscar producto..."
        className="border-2 border-purple-300 p-3 rounded-xl w-full mb-4 focus:outline-none focus:ring-2 focus:ring-purple-500 transition"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />

      <div className="flex gap-3 mb-4">
        <input
          type="text"
          placeholder="Nuevo producto"
          className="flex-1 border-2 border-purple-300 p-3 rounded-xl focus:outline-none focus:ring-2 focus:ring-purple-500 transition"
          value={newProduct}
          onChange={(e) => setNewProduct(e.target.value)}
        />
        <button
          onClick={handleAddProduct}
          className="bg-purple-600 hover:bg-purple-700 text-white px-6 py-3 rounded-xl shadow-lg transition transform hover:-translate-y-1"
        >
          Agregar
        </button>
      </div>

      {/* LISTA EN GRID CON SCROLL */}
      <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-3 overflow-y-auto flex-1 pr-2">
        {filteredProducts.map(p => (
          <li
            key={p.id}
            className="flex justify-between items-center bg-white rounded-lg px-3 py-2 shadow-sm border border-purple-200 hover:shadow-md transition"
          >
            <span className="text-purple-800 font-medium">{p.name}</span>
            <button
              onClick={() => handleDelete(p.id)}
              className="text-red-600 hover:text-red-800 transition"
            >
              ✕
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Products;
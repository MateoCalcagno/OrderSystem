import React, { useEffect, useState } from "react";

function Orders() {
  const [products, setProducts] = useState([]);
  const [searchProduct, setSearchProduct] = useState(""); // 🔹 estado búsqueda
  const [selectedProducts, setSelectedProducts] = useState([]);
  const [orders, setOrders] = useState([]);
  const [newOrderId, setNewOrderId] = useState(null);

  useEffect(() => {
    fetch("http://localhost:8080/products")
      .then(res => res.json())
      .then(data => setProducts(data))
      .catch(err => console.error(err));

    fetchOrders();
  }, []);

  const fetchOrders = () => {
    fetch("http://localhost:8080/orders")
      .then(res => res.json())
      .then(data => setOrders(data))
      .catch(err => console.error(err));
  };

  const handleSelect = (id, checked) => {
    if (checked) setSelectedProducts([...selectedProducts, id]);
    else setSelectedProducts(selectedProducts.filter(pid => pid !== id));
  };

  const handleCreateOrder = () => {
    if (selectedProducts.length === 0) return;
    fetch("http://localhost:8080/orders", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ productIds: selectedProducts })
    })
      .then(res => res.json())
      .then(data => {
        setOrders([...orders, data]);
        setSelectedProducts([]);
        setNewOrderId(data.id);
        setTimeout(() => setNewOrderId(null), 3000);
      })
      .catch(err => console.error(err));
  };

  const handleDeleteOrder = (id) => {
    fetch(`http://localhost:8080/orders/${id}`, { method: "DELETE" })
      .then(() => setOrders(orders.filter(o => o.id !== id)))
      .catch(err => console.error(err));
  };

  // 🔹 filtrar productos según búsqueda (empieza con)
  const filteredProducts = products.filter(p =>
    p.name.toLowerCase().startsWith(searchProduct.toLowerCase())
  );

  const productCount = {};
  orders.forEach(order => {
    order.products.forEach(p => {
      productCount[p] = (productCount[p] || 0) + 1;
    });
  });

  return (
    <div className="flex gap-6 h-full max-h-[450px]">
      {/* Columna izquierda: crear nueva orden */}
      <div className="flex flex-col flex-1 bg-gradient-to-r from-purple-100 via-purple-200 to-purple-300 rounded-2xl p-6 border border-purple-300 text-purple-900">
        <h2 className="text-2xl font-bold text-purple-900 mb-4">➕ Crear Nueva Orden</h2>

        {/* 🔹 Input búsqueda */}
        <input
          type="text"
          placeholder="Buscar producto..."
          className="border-2 border-purple-300 p-2 rounded-xl w-full mb-3 focus:outline-none focus:ring-2 focus:ring-purple-500 transition"
          value={searchProduct}
          onChange={(e) => setSearchProduct(e.target.value)}
        />

        {/* Lista de productos con scroll interno */}
        <div className="flex-1 overflow-y-auto mb-4 grid grid-cols-2 gap-2">
          {filteredProducts.map(p => (
            <label
              key={p.id}
              className="bg-white flex items-center gap-2 p-2 rounded-lg border border-purple-300 hover:bg-purple-50 cursor-pointer w-50 h-12 justify-start"
            >
              <input
                type="checkbox"
                checked={selectedProducts.includes(p.id)}
                onChange={(e) => handleSelect(p.id, e.target.checked)}
              />
              {p.name}
            </label>
          ))}
        </div>

        <button
          onClick={handleCreateOrder}
          className="bg-purple-600 hover:bg-purple-700 text-white px-4 py-2 rounded shadow-md transition"
        >
          Crear Orden
        </button>
      </div>

      {/* Columna derecha: órdenes existentes */}
      <div className="flex flex-col flex-1 bg-gradient-to-r from-purple-100 via-purple-200 to-purple-300 rounded-2xl p-6 border border-purple-300 text-purple-900">
        <h2 className="text-2xl font-bold text-purple-900 mb-4">🧾 Órdenes Existentes</h2>

        <ul className="flex-1 overflow-y-auto space-y-3">
          {orders.map(o => (
            <li
              key={o.id}
              className={`p-3 border rounded flex justify-between items-center transition ${
                o.id === newOrderId ? "bg-purple-200 animate-pulse shadow-inner" : "bg-white/80"
              }`}
            >
              <div className="flex flex-wrap gap-2">
                <strong>Orden #{o.id}:</strong>
                {o.products.map(p => (
                  <span
                    key={p}
                    className={`inline-block px-2 py-1 rounded text-sm ${
                      productCount[p] > 1 ? "bg-purple-300" : "bg-purple-100"
                    }`}
                  >
                    {p}
                  </span>
                ))}
              </div>
              <button
                onClick={() => handleDeleteOrder(o.id)}
                className="text-red-600 hover:text-red-800 transition"
              >
                Eliminar
              </button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default Orders;
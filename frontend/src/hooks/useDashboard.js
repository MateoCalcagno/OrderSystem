import { useEffect, useState } from "react";
import orderService from "../services/orderService";
import productService from "../services/productService";

export const useDashboard = () => {
  const [orders, setOrders] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        const [ordersData, productsData] = await Promise.all([
          orderService.getAll(),
          productService.getAll()
        ]);

        setOrders(ordersData);
        setProducts(productsData);
      } catch (err) {
        console.error("Error cargando dashboard", err);
      } finally {
        setLoading(false);
      }
    };

    load();
  }, []);

  // 🔥 lógica movida acá
  const productCount = {};
  orders.forEach(order => {
    order.products.forEach(name => {
      productCount[name] = (productCount[name] || 0) + 1;
    });
  });

  const chartData = Object.entries(productCount).map(([name, count]) => ({
    name,
    count
  }));

  return {
    orders,
    products,
    chartData,
    loading
  };
};
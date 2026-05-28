import { useEffect, useState } from "react";
import orderService from "../services/orderService";
import productService from "../services/productService";

export const useDashboard = () => {
  const [orders, setOrders] = useState([]);
  const [products, setProducts] = useState([]);
  const [chartData, setChartData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        const [ordersData, productsData] = await Promise.all([
          orderService.getAll(0, 1000),
          productService.getAll(0, 1000)
        ]);

        const ordersList = ordersData.content || [];
        const productsList = productsData.content || [];

        setOrders(ordersList);
        setProducts(productsList);

        const productCount = {};

        ordersList.forEach(order => {
          order.items?.forEach(item => {
            const name = item.productName || item.name;

            if (!name) return;

            productCount[name] =
              (productCount[name] || 0) + (item.quantity || 1);
          });
        });

        const chart = Object.entries(productCount).map(
          ([name, count]) => ({
            name,
            count
          })
        );

        setChartData(chart);
      } catch (err) {
        console.error("Error cargando dashboard", err);
      } finally {
        setLoading(false);
      }
    };

    load();
  }, []);

  return {
    orders,
    products,
    chartData,
    loading
  };
};
import { useEffect, useState } from "react";
import orderService from "../services/orderService";
import toast from "react-hot-toast";

export const useOrders = () => {
  const [orders, setOrders] = useState([]);

  const loadOrders = async () => {
    try {
      const data = await orderService.getAll();
      setOrders(data);
    } catch {
      toast.error("Error al cargar órdenes");
    }
  };

  const deleteOrder = async (id) => {
    try {
      await orderService.remove(id);
      setOrders(prev => prev.filter(o => o.id !== id));
      toast.success("Orden eliminada");
    } catch {
      toast.error("No se pudo eliminar");
    }
  };

  useEffect(() => {
    loadOrders();
  }, []);

  return {
    orders,
    deleteOrder
  };
};
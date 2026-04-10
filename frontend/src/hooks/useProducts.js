import { useEffect, useState } from "react";
import productService from "../services/productService";
import toast from "react-hot-toast";

export const useProducts = () => {
  const [products, setProducts] = useState([]);

  const loadProducts = async () => {
    try {
      const data = await productService.getAll();
      setProducts(data);
    } catch {
      toast.error("Error al cargar productos");
    }
  };

  const createProduct = async (name) => {
    if (!name.trim()) return;

    try {
      const created = await productService.create(name);
      setProducts(prev => [...prev, created]);
      toast.success("Producto creado 🏷️");
    } catch {
      toast.error("Solo administradores");
    }
  };

  const deleteProduct = async (id) => {
    try {
      await productService.remove(id);
      setProducts(prev => prev.filter(p => p.id !== id));
      toast.success("Eliminado");
    } catch {
      toast.error("Error al borrar");
    }
  };

  useEffect(() => {
    loadProducts();
  }, []);

  return {
    products,
    createProduct,
    deleteProduct
  };
};
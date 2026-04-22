import { useEffect, useState } from "react";
import productService from "../services/productService";
import toast from "react-hot-toast";

export const useProducts = () => {
  const [products, setProducts] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const loadProducts = async (page = 0, search = "") => {
    try {
      const data = await productService.getAll(page, 4, search);
      setProducts(data.content);
      setTotalPages(data.totalPages);
      setCurrentPage(data.number);
    } catch {
      toast.error("Error al cargar productos");
    }
  };

  const createProduct = async (name, price) => {
    if (!name.trim()) return;
    if (!price || isNaN(price) || Number(price) <= 0) {
      toast.error("El precio debe ser mayor a 0");
      return;
    }

    try {
      const created = await productService.create(name, Number(price));
      setProducts(prev => [...prev, created]);
      toast.success("Producto creado 🏷️");
    } catch {
      toast.error("Error al crear el producto");
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
    deleteProduct,
    currentPage,
    totalPages,
    loadProducts
  };
};
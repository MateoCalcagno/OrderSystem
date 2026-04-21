import api from "./api";
import request from "./request";

const getAll = () => request(api.get("/products"));

const create = (name, price) =>
  request(api.post("/products", { name, price }));

const remove = (id) => api.delete(`/products/${id}`);

const productService = {
  getAll,
  create,
  remove,
};

export default productService;
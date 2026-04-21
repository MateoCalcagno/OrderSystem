import api from "./api";
import request from "./request";

const getAll = (page = 0, size = 4) =>
  request(api.get("/products", { params: { page, size } }));

const create = (name, price) =>
  request(api.post("/products", { name, price }));

const remove = (id) => api.delete(`/products/${id}`);

const productService = {
  getAll,
  create,
  remove,
};

export default productService;
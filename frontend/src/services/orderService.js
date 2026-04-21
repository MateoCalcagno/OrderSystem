import api from "./api";
import request from "./request";

const getAll = (page = 0, size = 4) =>
  request(api.get("/orders", { params: { page, size } }));

const create = (productIds) =>
  request(api.post("/orders", { productIds }));

const remove = (id) => api.delete(`/orders/${id}`);

const orderService = {
  getAll,
  create,
  remove,
};

export default orderService;
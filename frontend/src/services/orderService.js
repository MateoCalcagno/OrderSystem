import api from "./api";
import request from "./request";

const getAll = () => request(api.get("/orders"));

const create = (productIds) =>
  request(api.post("/orders", { productIds }));

const remove = (id) => api.delete(`/orders/${id}`);

const orderService = {
  getAll,
  create,
  remove,
};

export default orderService;
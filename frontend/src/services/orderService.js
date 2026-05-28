import api from "./api";
import request from "./request";
 
const getAll = (page = 0, size = 4) =>
  request(api.get("/orders", { params: { page, size } }));
 
// items con productId + quantity [{ productId: 1, quantity: 2 }, { productId: 2, quantity: 1 }]
const create = (items) =>
  request(api.post("/orders", { items }));
 
const remove = (id) => api.delete(`/orders/${id}`);
 
const orderService = {
  getAll,
  create,
  remove,
};
 
export default orderService;
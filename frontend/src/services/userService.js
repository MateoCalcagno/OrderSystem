import api from "./api";
import request from "./request";

const login = (username, password) =>
  request(api.post("/users/login", { username, password }));

const register = (data) =>
  request(api.post("/users/register", data));

const getAll = () => request(api.get("/users"));

const userService = {
  login,
  register,
  getAll,
};

export default userService;
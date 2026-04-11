import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_URL,
});

// REQUEST → agrega token
api.interceptors.request.use((config) => {
  const savedUser = JSON.parse(localStorage.getItem("user"));

  if (savedUser && savedUser.token) {
    config.headers.Authorization = `Bearer ${savedUser.token}`;
  }

  return config;
});

// RESPONSE → manejar expiración
api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem("user");
      window.location.href = "/login";
    }
    return Promise.reject(err);
  }
);

export default api;
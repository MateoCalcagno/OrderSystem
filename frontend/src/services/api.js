import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
});

// Interceptor para pegar el Token automáticamente en cada petición
api.interceptors.request.use((config) => {
  const savedUser = JSON.parse(localStorage.getItem("user"));
  if (savedUser && savedUser.authHeader) {
    config.headers.Authorization = savedUser.authHeader;
  }
  return config;
});

export default api;
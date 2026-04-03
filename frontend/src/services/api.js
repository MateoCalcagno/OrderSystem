import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
});

api.interceptors.request.use((config) => {
  const savedUser = JSON.parse(localStorage.getItem("user"));
  
  // Si existe el usuario y tiene un token, lo mandamos como Bearer
  if (savedUser && savedUser.token) {
    config.headers.Authorization = `Bearer ${savedUser.token}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

export default api;
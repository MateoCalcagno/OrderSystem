import React, { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    
    // Creamos el header temporal solo para validar
    const authHeader = "Basic " + btoa(`${username}:${password}`);

    try {
      // Intentamos obtener los productos como prueba de login
      const response = await api.get("/products", {
        headers: { "Authorization": authHeader }
      });

      if (response.status === 200) {
        // Buscamos el rol del usuario (podrías tener un endpoint /me, 
        // pero como tenemos /users, buscamos ahí)
        const usersRes = await api.get("/users", {
          headers: { "Authorization": authHeader }
        });
        
        const foundUser = usersRes.data.find(u => u.username === username);

        login({
          username,
          role: foundUser ? foundUser.role : "USER",
          authHeader
        });
        navigate("/products");
      }
    } catch (err) {
      setError("Usuario o contraseña incorrectos");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center h-full">
      <div className="bg-white/80 backdrop-blur-md p-8 rounded-3xl shadow-2xl border border-purple-200 w-full max-w-md">
        <h2 className="text-3xl font-extrabold text-purple-900 mb-6 text-center">Iniciar Sesión</h2>
        {error && <p className="bg-red-100 text-red-600 p-3 rounded-xl mb-4 text-center text-sm">{error}</p>}
        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="text"
            placeholder="Usuario"
            className="w-full p-3 rounded-xl border-2 border-purple-200 focus:ring-2 focus:ring-purple-500 outline-none"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Contraseña"
            className="w-full p-3 rounded-xl border-2 border-purple-200 focus:ring-2 focus:ring-purple-500 outline-none"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button type="submit" className="w-full bg-purple-600 text-white font-bold py-3 rounded-xl hover:bg-purple-700 transition">
            Entrar
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
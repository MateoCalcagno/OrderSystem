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
    
    try {
      // 1. Llamamos al endpoint de login que devuelve el TOKEN
      const response = await api.post("/users/login", { username, password });
      const { token } = response.data;

      // 2. Con el token, podemos pedir la info de los usuarios para saber el rol
      // (Opcional: podrías modificar tu backend para que el login ya devuelva el rol)
      const usersRes = await api.get("/users", {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      const foundUser = usersRes.data.find(u => u.username === username);

      login({
        username,
        role: foundUser ? foundUser.role : "USER",
        token // Guardamos el JWT
      });
      
      navigate("/products");
    } catch (err) {
      setError("Credenciales inválidas");
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

          <p className="text-center mt-6 text-sm text-purple-800">
            ¿No tenés cuenta?{" "}
            <button
              type="button" // IMPORTANTE: type="button" para que NO intente hacer login al tocarlo
              onClick={() => navigate("/signup")}
              className="text-purple-600 cursor-pointer font-bold hover:underline bg-transparent border-none p-0"
            >
              Registrate
            </button>
          </p>
        </form>
      </div>
    </div>
  );
}

export default Login;
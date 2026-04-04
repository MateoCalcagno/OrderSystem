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
      const response = await api.post("/users/login", { username, password });
      const { token } = response.data;

      const usersRes = await api.get("/users", {
        headers: { Authorization: `Bearer ${token}` }
      });

      const foundUser = usersRes.data.find(u => u.username === username);

      login({
        username,
        role: foundUser ? foundUser.role : "USER",
        token
      });

      navigate("/products");

    } catch (err) {
      setError("Credenciales inválidas ❌");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center h-full">

      <div className="bg-white/10 backdrop-blur-xl p-8 rounded-3xl shadow-2xl border border-white/10 w-full max-w-md text-white">
        
        <h2 className="text-3xl font-extrabold mb-6 text-center">
          Iniciar Sesión
        </h2>

        {error && (
          <p className="bg-red-500/20 text-red-400 border border-red-500/20 p-3 rounded-xl mb-4 text-center text-sm">
            {error}
          </p>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">

          <input
            type="text"
            placeholder="Usuario"
            className="w-full p-3 rounded-xl bg-white/10 border border-white/20 text-white placeholder:text-white/40 focus:ring-2 focus:ring-purple-500 outline-none"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />

          <input
            type="password"
            placeholder="Contraseña"
            className="w-full p-3 rounded-xl bg-white/10 border border-white/20 text-white placeholder:text-white/40 focus:ring-2 focus:ring-purple-500 outline-none"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button 
            type="submit"
            className="w-full bg-gradient-to-r from-purple-600 to-pink-600 text-white font-bold py-3 rounded-xl hover:scale-105 active:scale-95 transition-all shadow-lg"
          >
            Entrar
          </button>

          <p className="text-center mt-4 text-sm text-white/70">
            ¿No tenés cuenta?{" "}
            <button
              type="button"
              onClick={() => navigate("/signup")}
              className="text-white font-bold hover:underline"
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
import React, { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import userService from "../services/userService";
import Input from "../components/ui/Input";
import Button from "../components/ui/Button";

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
      const data = await userService.login(username, password);

      login({
        username: data.username,
        role: data.role,
        token: data.token
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

          <Input
            type="text"
            placeholder="Usuario"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />

          <Input
            type="password"
            placeholder="Contraseña"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <Button
            type="submit"
            className="w-full bg-gradient-to-r from-purple-600 to-pink-600 text-white py-3 hover:scale-105"
          >
            Entrar
          </Button>

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
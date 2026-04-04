import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

function SignUp() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    email: "",
    dni: "",
    firstName: "",
    lastName: ""
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const navigate = useNavigate();

  const inputStyle = "w-full p-3 rounded-xl bg-white/10 border border-white/20 text-white placeholder:text-white/40 focus:ring-2 focus:ring-purple-500 outline-none";

  const handleChange = (e) => {
    setFormData({ 
      ...formData, 
      [e.target.name]: e.target.value 
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    try {
      await api.post("/users/register", formData);

      setSuccess("Cuenta creada con éxito 🎉");

      setTimeout(() => {
        navigate("/login");
      }, 1200);

    } catch (err) {
      const serverError = err.response?.data?.error || "Error al registrarse";
      setError(serverError);
    }
  };

  return (
    <div className="flex items-center justify-center h-full">

      <div className="bg-white/10 backdrop-blur-xl p-6 rounded-3xl shadow-2xl border border-white/10 w-full max-w-lg text-white">
        
        <h2 className="text-3xl font-extrabold mb-6 text-center">
          Crear Cuenta
        </h2>

        {error && (
          <p className="bg-red-500/20 text-red-400 border border-red-500/20 p-3 rounded-xl mb-4 text-center text-sm">
            {error}
          </p>
        )}

        {success && (
          <p className="bg-green-500/20 text-green-400 border border-green-500/20 p-3 rounded-xl mb-4 text-center text-sm">
            {success}
          </p>
        )}

        <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-3">

          <input name="firstName" placeholder="Nombre" className={inputStyle} onChange={handleChange} required />
          <input name="lastName" placeholder="Apellido" className={inputStyle} onChange={handleChange} required />

          <input name="username" placeholder="Usuario" className={`${inputStyle} md:col-span-2`} onChange={handleChange} required />
          <input name="email" type="email" placeholder="Email" className={`${inputStyle} md:col-span-2`} onChange={handleChange} required />

          <input name="dni" placeholder="DNI" className={inputStyle} onChange={handleChange} required />
          <input name="password" type="password" placeholder="Contraseña" className={inputStyle} onChange={handleChange} required />

          <button 
            type="submit"
            className="md:col-span-2 bg-gradient-to-r from-purple-600 to-pink-600 text-white font-bold py-3 rounded-xl hover:scale-105 active:scale-95 transition-all shadow-lg mt-3"
          >
            Registrarse
          </button>

        </form>

        <p className="text-center mt-4 text-sm text-white/70">
          ¿Ya tenés cuenta?{" "}
          <button
            onClick={() => navigate("/login")}
            className="text-white font-bold hover:underline"
          >
            Iniciar Sesión
          </button>
        </p>

      </div>
    </div>
  );
}

export default SignUp;
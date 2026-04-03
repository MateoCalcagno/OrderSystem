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

  // Constante de estilo para evitar repetición y warnings de ESLint
  const inputStyle = "w-full p-3 rounded-xl border-2 border-purple-100 focus:border-purple-500 focus:ring-2 focus:ring-purple-200 outline-none transition-all bg-white/50 placeholder:text-purple-300 text-purple-900";

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
      // Enviamos el objeto formData que ya tiene la estructura del RegisterDTO
      await api.post("/users/register", formData);

      setSuccess("¡Cuenta creada con éxito! Redirigiendo al login...");
      
      // Redirige al login después de 1.5 segundos para que el usuario vea el mensaje de éxito
      setTimeout(() => {
        navigate("/login");
      }, 1500);

    } catch (err) {
      // Intentamos capturar el mensaje de error que viene del GlobalExceptionHandler de Spring
      const serverError = err.response?.data?.error || "Error al registrar usuario. Verifica los datos.";
      setError(serverError);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-full py-8">
      <div className="bg-white/80 backdrop-blur-md p-8 rounded-3xl shadow-2xl border border-purple-200 w-full max-w-lg">
        
        <h2 className="text-3xl font-extrabold text-purple-900 mb-6 text-center">
          Crear Cuenta
        </h2>

        {error && (
          <p className="bg-red-100 text-red-600 p-3 rounded-xl mb-4 text-center text-sm font-medium">
            {error}
          </p>
        )}
        
        {success && (
          <p className="bg-green-100 text-green-600 p-3 rounded-xl mb-4 text-center text-sm font-medium">
            {success}
          </p>
        )}

        <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-4">
          
          <input
            name="firstName"
            type="text"
            placeholder="Nombre"
            className={inputStyle}
            onChange={handleChange}
            required
          />

          <input
            name="lastName"
            type="text"
            placeholder="Apellido"
            className={inputStyle}
            onChange={handleChange}
            required
          />

          <input
            name="username"
            type="text"
            placeholder="Nombre de Usuario"
            className={`${inputStyle} md:col-span-2`}
            onChange={handleChange}
            required
          />

          <input
            name="email"
            type="email"
            placeholder="Correo Electrónico"
            className={`${inputStyle} md:col-span-2`}
            onChange={handleChange}
            required
          />

          <input
            name="dni"
            type="text"
            placeholder="DNI"
            className={inputStyle}
            onChange={handleChange}
            required
          />

          <input
            name="password"
            type="password"
            placeholder="Contraseña"
            className={inputStyle}
            onChange={handleChange}
            required
          />

          <button 
            type="submit"
            className="md:col-span-2 bg-gradient-to-r from-purple-600 to-pink-600 text-white font-bold py-4 rounded-xl hover:scale-[1.02] active:scale-95 transition-all shadow-lg mt-4"
          >
            Registrarse Ahora
          </button>

        </form>

        <p className="text-center mt-6 text-sm text-purple-800">
          ¿Ya eres parte del sistema?{" "}
          <button
              type="button" 
              onClick={() => navigate("/login")}
              className="text-purple-600 cursor-pointer font-bold hover:underline bg-transparent border-none p-0"
          >
            Iniciar Sesión
          </button>
        </p>
      </div>
    </div>
  );
}

export default SignUp;
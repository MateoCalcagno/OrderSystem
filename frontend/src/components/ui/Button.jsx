const Button = ({ children, onClick, variant = "primary", className = "", ...props }) => {

  const base = "px-4 py-2 rounded-xl font-semibold transition-all duration-200";

  const variants = {
    primary: "bg-gradient-to-r from-purple-600 to-pink-600 text-white hover:scale-105 active:scale-95",
    danger: "bg-red-500/20 text-red-400 hover:bg-red-500 hover:text-white",
    success: "bg-green-500/20 text-green-400 hover:bg-green-500 hover:text-white"
  };

  return (
    <button
      onClick={onClick}
      className={`${base} ${variants[variant]} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
};

export default Button;
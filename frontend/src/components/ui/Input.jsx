const Input = ({ value, onChange, placeholder, type = "text", className = "", ...props }) => {
  return (
    <input
      type={type}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      className={`w-full p-3 rounded-xl bg-white/10 border border-white/20 text-white placeholder:text-white/40 focus:ring-2 focus:ring-purple-500 outline-none ${className}`}
      {...props}
    />
  );
};

export default Input;
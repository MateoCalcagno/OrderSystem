const Card = ({ children, className = "" }) => {
  return (
    <div className={`bg-white/10 backdrop-blur-xl p-6 rounded-2xl border border-white/10 shadow-lg ${className}`}>
      {children}
    </div>
  );
};

export default Card;
import React from "react";
import { useAuth } from "../context/AuthContext";
import api from "../services/api";
import toast from "react-hot-toast";

function CartSidebar() {
  const { cart, updateQuantity, removeFromCart, clearCart, user } = useAuth();

  if (!user || user.role !== "USER") return null;

  const handleCreateOrder = async () => {
    if (cart.length === 0) return;

    const loadId = toast.loading("Procesando tu pedido...");

    try {
      const productIds = [];
      cart.forEach(item => {
        for (let i = 0; i < item.quantity; i++) productIds.push(item.id);
      });

      await api.post("/orders", { productIds });
      
      clearCart(); // Limpia el carrito sin recargar
      toast.success("¡Pedido confirmado! 🎉", { id: loadId });
      
    } catch (err) {
      toast.error("Error al procesar el pedido ❌", { id: loadId });
    }
  };

  return (
    <aside className="bg-white/5 backdrop-blur-xl border border-white/10 rounded-2xl flex flex-col h-full shadow-xl overflow-hidden animate-in fade-in slide-in-from-right-5">
      <div className="p-5 border-b border-white/10 bg-white/5 flex justify-between items-center">
        <h3 className="text-lg font-bold text-white flex items-center gap-2">🛒 Carrito</h3>
        <span className="bg-purple-600/30 text-purple-300 text-[10px] px-2 py-1 rounded-md border border-purple-500/20">
          {cart.reduce((acc, item) => acc + item.quantity, 0)} ITEMS
        </span>
      </div>

      <div className="flex-1 overflow-y-auto p-4 space-y-3 scrollbar-hide">
        {cart.length === 0 ? (
          <div className="h-full flex flex-col items-center justify-center opacity-20 italic text-sm text-center">
            <p>Tu carrito está vacío</p>
          </div>
        ) : (
          cart.map((item) => (
            <div key={item.id} className="bg-white/5 p-3 rounded-xl border border-white/5 flex flex-col gap-3 group animate-in zoom-in-95 duration-200">
              <div className="flex justify-between items-center">
                <span className="text-xs font-bold text-white truncate w-36">{item.name}</span>
                <button 
                  onClick={() => {
                    removeFromCart(item.id);
                    toast.error(`${item.name} eliminado`, { duration: 1500 });
                  }} 
                  className="text-red-400 hover:text-red-500 transition-colors"
                >✕</button>
              </div>
              <div className="flex items-center justify-between bg-black/30 rounded-lg p-1">
                <button onClick={() => updateQuantity(item.id, -1)} className="w-7 h-7 flex items-center justify-center hover:bg-white/10 rounded-md">-</button>
                <span className="text-purple-400 font-black text-sm">{item.quantity}</span>
                <button onClick={() => updateQuantity(item.id, 1)} className="w-7 h-7 flex items-center justify-center hover:bg-white/10 rounded-md">+</button>
              </div>
            </div>
          ))
        )}
      </div>

      {cart.length > 0 && (
        <div className="p-4 bg-white/5 border-t border-white/10">
          <button 
            onClick={handleCreateOrder} 
            className="w-full bg-gradient-to-r from-purple-600 to-pink-600 py-4 rounded-xl font-black text-white text-xs uppercase tracking-widest shadow-lg hover:scale-[1.02] active:scale-95 transition-all"
          >Confirmar Compra</button>
        </div>
      )}
    </aside>
  );
}

export default CartSidebar;
// frontend/src/contexts/cart-context.tsx
"use client";

import { createContext, useContext, useEffect, useState } from 'react';
import { cartService } from '@/lib/firestore';
import { Product } from '@/lib/types';
import { useAuth } from './auth-context';

interface CartContextType {
  cartItems: (Product & { quantity: number })[];
  addToCart: (product: Product) => Promise<void>;
  removeFromCart: (productId: string) => Promise<void>;
  updateQuantity: (productId: string, quantity: number) => Promise<void>;
  loading: boolean;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider = ({ children }: { children: React.ReactNode }) => {
  const [cartItems, setCartItems] = useState<(Product & { quantity: number })[]>([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  const loadCart = async () => {
    if (!user) {
      setCartItems([]);
      setLoading(false);
      return;
    }

    try {
      setLoading(true);
      const items = await cartService.getCart();
      setCartItems(items as (Product & { quantity: number })[]);
    } catch (error) {
      console.error('Error loading cart:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCart();
  }, [user]);

  const addToCart = async (product: Product) => {
    try {
      await cartService.addToCart({
        ...product,
        quantity: 1 // Default quantity when adding to cart
      });
      await loadCart();
    } catch (error) {
      console.error('Error adding to cart:', error);
      throw error;
    }
  };

  const removeFromCart = async (productId: string) => {
    try {
      await cartService.removeFromCart(productId);
      await loadCart();
    } catch (error) {
      console.error('Error removing from cart:', error);
      throw error;
    }
  };

  const updateQuantity = async (productId: string, quantity: number) => {
    if (quantity < 1) {
      await removeFromCart(productId);
      return;
    }

    try {
      const item = cartItems.find(item => item.id === productId);
      if (!item) return;

      await cartService.addToCart({
        ...item,
        quantity
      });
      
      await loadCart();
    } catch (error) {
      console.error('Error updating quantity:', error);
      throw error;
    }
  };

  return (
    <CartContext.Provider 
      value={{
        cartItems,
        addToCart,
        removeFromCart,
        updateQuantity,
        loading
      }}
    >
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const context = useContext(CartContext);
  if (context === undefined) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};
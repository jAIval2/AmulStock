"use client";

import { AuthProvider } from '@/contexts/auth-context';
import { CartProvider } from '@/contexts/cart-context';
import { ThemeProvider as NextThemesProvider } from 'next-themes';
import { type ThemeProviderProps } from 'next-themes';
import { SimpleToastProvider } from './SimpleToast';

export function Providers({ children, ...props }: ThemeProviderProps) {
  return (
    <NextThemesProvider {...props}>
      <AuthProvider>
        <CartProvider>
          <SimpleToastProvider> 
            {children}
          </SimpleToastProvider>
        </CartProvider>
      </AuthProvider>
    </NextThemesProvider>
  );
}

"use client";

import { AuthProvider } from '@/contexts/auth-context';
import { ThemeProvider as NextThemesProvider } from 'next-themes';
import { type ThemeProviderProps } from 'next-themes';

export function Providers({ children, ...props }: ThemeProviderProps) {
  return (
    <NextThemesProvider {...props}>
      <AuthProvider>
        {children}
      </AuthProvider>
    </NextThemesProvider>
  );
}

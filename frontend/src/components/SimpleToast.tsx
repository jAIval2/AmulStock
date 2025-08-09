import React, { createContext, useContext, useState, useCallback } from "react";

type ToastType = { message: string; type?: "success" | "error" };

const SimpleToastContext = createContext<{ showToast: (msg: string, type?: "success" | "error") => void }>({ showToast: () => {} });

export const useSimpleToast = () => useContext(SimpleToastContext);

export const SimpleToastProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [toast, setToast] = useState<ToastType | null>(null);

  const showToast = useCallback((message: string, type: "success" | "error" = "success") => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 2500);
  }, []);

  return (
    <SimpleToastContext.Provider value={{ showToast }}>
      {children}
      {toast && (
        <div
          style={{
            position: "fixed",
            bottom: 32,
            left: "50%",
            transform: "translateX(-50%)",
            background: toast.type === "error" ? "#ef4444" : "#22c55e",
            color: "#fff",
            padding: "12px 24px",
            borderRadius: 8,
            boxShadow: "0 2px 8px rgba(0,0,0,0.15)",
            zIndex: 9999,
            fontWeight: 500,
            fontSize: 16,
            transition: "opacity 0.2s",
          }}
        >
          {toast.message}
        </div>
      )}
    </SimpleToastContext.Provider>
  );
};
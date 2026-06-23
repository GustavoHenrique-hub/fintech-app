import { createContext, useContext, useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import * as AuthService from "@/services/auth/AuthService";
import { TOKEN_KEY, USER_KEY } from "@/lib/constants";

function readUser() {
  try { return JSON.parse(localStorage.getItem(USER_KEY)); } catch { return null; }
}

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem(TOKEN_KEY));
  const [user,  setUser]  = useState(readUser);
  const navigate = useNavigate();

  const clearSession = useCallback(() => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    setToken(null);
    setUser(null);
  }, []);

  const saveSession = useCallback((data) => {
    const u = {
      idUsuario:    data.idUsuario,
      usuarioCode:  data.usuarioCode,
      expiraEm:     data.expiraEm,
    };
    localStorage.setItem(TOKEN_KEY, data.token);
    localStorage.setItem(USER_KEY, JSON.stringify(u));
    setToken(data.token);
    setUser(u);
  }, []);

  // interceptor 401 dispara esse evento → limpa sessão e redireciona
  useEffect(() => {
    const handler = () => {
      clearSession();
      navigate("/login", { replace: true });
    };
    window.addEventListener("auth:logout", handler);
    return () => window.removeEventListener("auth:logout", handler);
  }, [clearSession, navigate]);

  const logout = useCallback(async () => {
    if (token) {
      try { await AuthService.logout(token); } catch { /* ignora erros de rede */ }
    }
    clearSession();
    navigate("/login", { replace: true });
  }, [token, clearSession, navigate]);

  return (
    <AuthContext.Provider
      value={{ token, user, isAuthenticated: !!token, saveSession, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth deve ser usado dentro de AuthProvider");
  return ctx;
}

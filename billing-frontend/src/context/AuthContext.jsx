import { createContext, useContext, useState, useCallback } from 'react';
import { authService } from '../api/authService';
import { decodeToken } from '../api/jwt';

const AuthContext = createContext(null);

function userFromToken(token) {
  if (!token) return null;
  const payload = decodeToken(token);
  if (!payload) return null;
  return {
    username: payload.sub || payload.username,
    // Adjust this claim name to whatever your JWT actually stores roles under
    // (e.g. "roles", "authorities", "role").
    roles: payload.roles || payload.authorities || [],
  };
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('token'));
  const [user, setUser] = useState(() => userFromToken(localStorage.getItem('token')));

  const login = useCallback(async (username, password) => {
    const { data } = await authService.login(username, password);
    // Adjust this if your login response wraps the token differently,
    // e.g. { accessToken: "..." } instead of { token: "..." }.
    const jwt = data.token || data.accessToken;
    localStorage.setItem('token', jwt);
    setToken(jwt);
    setUser(userFromToken(jwt));
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
  }, []);

  const isAdmin = user?.roles?.some((r) => r.includes('ADMIN'));

  return (
    <AuthContext.Provider value={{ token, user, isAdmin, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}

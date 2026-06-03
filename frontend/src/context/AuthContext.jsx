import { createContext, useCallback, useContext, useMemo, useState } from 'react';
import { authService } from '../api/authService';

const AuthContext = createContext(null);

const TOKEN_KEY = 'sp_token';
const USERNAME_KEY = 'sp_username';
const ROLE_KEY = 'sp_role';

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem(TOKEN_KEY));
  const [username, setUsername] = useState(() => localStorage.getItem(USERNAME_KEY));
  const [role, setRole] = useState(() => localStorage.getItem(ROLE_KEY));

  const isAuthenticated = Boolean(token);
  const isAdmin = role === 'ADMIN';
  const isAttendant = role === 'ATTENDANT';

  const login = useCallback(async (credentials) => {
    const { data } = await authService.login(credentials);
    localStorage.setItem(TOKEN_KEY, data.token);
    localStorage.setItem(USERNAME_KEY, data.username);
    localStorage.setItem(ROLE_KEY, data.role);
    setToken(data.token);
    setUsername(data.username);
    setRole(data.role);
    return data;
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USERNAME_KEY);
    localStorage.removeItem(ROLE_KEY);
    setToken(null);
    setUsername(null);
    setRole(null);
  }, []);

  const value = useMemo(
    () => ({
      token,
      username,
      role,
      isAuthenticated,
      isAdmin,
      isAttendant,
      login,
      logout,
    }),
    [token, username, role, isAuthenticated, isAdmin, isAttendant, login, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
}

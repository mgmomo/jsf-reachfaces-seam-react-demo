import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { fetchCurrentUser } from '../api/authApi';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const refresh = useCallback(async () => {
    try {
      const data = await fetchCurrentUser();
      setUser(data);
    } catch {
      setUser({ username: 'anonymous', role: 'GUEST' });
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    refresh();
  }, [refresh]);

  const login = useCallback((username, role) => {
    localStorage.setItem('auth_user', username);
    localStorage.setItem('auth_role', role);
    setUser({ username, role });
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('auth_user');
    localStorage.removeItem('auth_role');
    setUser({ username: 'anonymous', role: 'GUEST' });
  }, []);

  const isAdmin = user?.role === 'ADMIN';
  const isUser = user?.role === 'USER' || user?.role === 'ADMIN';

  return (
    <AuthContext.Provider value={{ user, loading, isAdmin, isUser, login, logout, refresh }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}

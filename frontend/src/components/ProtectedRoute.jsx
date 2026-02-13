import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function ProtectedRoute({ requiredRole, children }) {
  const { user, loading, isAdmin, isUser } = useAuth();

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  if (requiredRole === 'ADMIN' && !isAdmin) {
    return <Navigate to="/access-denied" replace />;
  }

  if (requiredRole === 'USER' && !isUser) {
    return <Navigate to="/access-denied" replace />;
  }

  return children;
}

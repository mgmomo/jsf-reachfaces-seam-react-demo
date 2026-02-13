import { Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import ProtectedRoute from './components/ProtectedRoute';
import HomePage from './pages/HomePage';
import PersonListPage from './pages/PersonListPage';
import PersonEditPage from './pages/PersonEditPage';
import LocationListPage from './pages/LocationListPage';
import LocationEditPage from './pages/LocationEditPage';
import AccessDeniedPage from './pages/AccessDeniedPage';
import AboutPage from './pages/AboutPage';

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route path="/" element={<HomePage />} />
        <Route path="/about" element={<AboutPage />} />
        <Route path="/access-denied" element={<AccessDeniedPage />} />
        <Route
          path="/persons"
          element={
            <ProtectedRoute requiredRole="USER">
              <PersonListPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/persons/new"
          element={
            <ProtectedRoute requiredRole="ADMIN">
              <PersonEditPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/persons/:id/edit"
          element={
            <ProtectedRoute requiredRole="ADMIN">
              <PersonEditPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/locations"
          element={
            <ProtectedRoute requiredRole="USER">
              <LocationListPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/locations/new"
          element={
            <ProtectedRoute requiredRole="ADMIN">
              <LocationEditPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/locations/:id/edit"
          element={
            <ProtectedRoute requiredRole="ADMIN">
              <LocationEditPage />
            </ProtectedRoute>
          }
        />
      </Route>
    </Routes>
  );
}

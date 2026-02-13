import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { fetchLocations, deleteLocation } from '../api/locationApi';
import { useAuth } from '../context/AuthContext';

export default function LocationListPage() {
  const [locations, setLocations] = useState([]);
  const [error, setError] = useState(null);
  const { isAdmin } = useAuth();

  useEffect(() => {
    loadLocations();
  }, []);

  async function loadLocations() {
    try {
      setError(null);
      const data = await fetchLocations();
      setLocations(data);
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleDelete(id, name) {
    if (!confirm(`Delete location "${name}"?`)) return;
    try {
      await deleteLocation(id);
      loadLocations();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div>
      <div className="page-header">
        <h2>Locations</h2>
        {isAdmin && (
          <Link to="/locations/new" className="btn">Add Location</Link>
        )}
      </div>

      {error && <div className="error-msg">{error}</div>}

      <table className="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Address</th>
            <th>Zip Code</th>
            <th>State</th>
            {isAdmin && <th>Actions</th>}
          </tr>
        </thead>
        <tbody>
          {locations.map((l) => (
            <tr key={l.id}>
              <td>{l.id}</td>
              <td>{l.locationName}</td>
              <td>{l.address || ''}</td>
              <td>{l.zipCode || ''}</td>
              <td>{l.stateLabel}</td>
              {isAdmin && (
                <td className="actions">
                  <Link to={`/locations/${l.id}/edit`} className="btn btn-sm">Edit</Link>
                  <button onClick={() => handleDelete(l.id, l.locationName)} className="btn btn-sm btn-danger">Delete</button>
                </td>
              )}
            </tr>
          ))}
          {locations.length === 0 && (
            <tr><td colSpan={isAdmin ? 6 : 5} className="empty">No locations found.</td></tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

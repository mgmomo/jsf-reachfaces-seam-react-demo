import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchLocation, createLocation, updateLocation } from '../api/locationApi';

export default function LocationEditPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isNew = !id;

  const [form, setForm] = useState({
    locationName: '',
    address: '',
    zipCode: '',
    state: 'ACTIVE',
  });
  const [error, setError] = useState(null);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (!isNew) {
      loadLocation();
    }
  }, [id]);

  async function loadLocation() {
    try {
      const data = await fetchLocation(id);
      setForm({
        locationName: data.locationName || '',
        address: data.address || '',
        zipCode: data.zipCode || '',
        state: data.state || 'ACTIVE',
      });
    } catch (err) {
      setError(err.message);
    }
  }

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setSaving(true);
    setError(null);
    try {
      if (isNew) {
        await createLocation(form);
      } else {
        await updateLocation(id, form);
      }
      navigate('/locations');
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <div>
      <h2>{isNew ? 'Add Location' : 'Edit Location'}</h2>

      {error && <div className="error-msg">{error}</div>}

      <form onSubmit={handleSubmit} className="edit-form">
        <div className="form-group">
          <label htmlFor="locationName">Location Name *</label>
          <input
            id="locationName"
            name="locationName"
            value={form.locationName}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="address">Address</label>
          <input
            id="address"
            name="address"
            value={form.address}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label htmlFor="zipCode">Zip Code</label>
          <input
            id="zipCode"
            name="zipCode"
            value={form.zipCode}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label htmlFor="state">State</label>
          <select id="state" name="state" value={form.state} onChange={handleChange}>
            <option value="ACTIVE">Active</option>
            <option value="NOT_ACTIVE">Not Active</option>
          </select>
        </div>

        <div className="form-actions">
          <button type="submit" className="btn" disabled={saving}>
            {saving ? 'Saving...' : 'Save'}
          </button>
          <button type="button" className="btn btn-secondary" onClick={() => navigate('/locations')}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}

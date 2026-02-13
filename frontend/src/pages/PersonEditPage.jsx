import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchPerson, createPerson, updatePerson } from '../api/personApi';
import { fetchLocations } from '../api/locationApi';

export default function PersonEditPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isNew = !id;

  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    dateOfBirth: '',
    locationIds: [],
  });
  const [allLocations, setAllLocations] = useState([]);
  const [error, setError] = useState(null);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    loadLocations();
    if (!isNew) {
      loadPerson();
    }
  }, [id]);

  async function loadLocations() {
    try {
      const data = await fetchLocations(true);
      setAllLocations(data);
    } catch (err) {
      setError(err.message);
    }
  }

  async function loadPerson() {
    try {
      const data = await fetchPerson(id);
      setForm({
        firstName: data.firstName || '',
        lastName: data.lastName || '',
        dateOfBirth: data.dateOfBirth || '',
        locationIds: data.locations ? data.locations.map((l) => l.id) : [],
      });
    } catch (err) {
      setError(err.message);
    }
  }

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function handleLocationToggle(locId) {
    setForm((prev) => {
      const ids = prev.locationIds.includes(locId)
        ? prev.locationIds.filter((x) => x !== locId)
        : [...prev.locationIds, locId];
      return { ...prev, locationIds: ids };
    });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setSaving(true);
    setError(null);
    try {
      if (isNew) {
        await createPerson(form);
      } else {
        await updatePerson(id, form);
      }
      navigate('/persons');
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  }

  return (
    <div>
      <h2>{isNew ? 'Add Person' : 'Edit Person'}</h2>

      {error && <div className="error-msg">{error}</div>}

      <form onSubmit={handleSubmit} className="edit-form">
        <div className="form-group">
          <label htmlFor="firstName">First Name *</label>
          <input
            id="firstName"
            name="firstName"
            value={form.firstName}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="lastName">Last Name *</label>
          <input
            id="lastName"
            name="lastName"
            value={form.lastName}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="dateOfBirth">Date of Birth</label>
          <input
            id="dateOfBirth"
            name="dateOfBirth"
            type="date"
            value={form.dateOfBirth}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label>Locations</label>
          <div className="checkbox-group">
            {allLocations.map((loc) => (
              <label key={loc.id} className="checkbox-label">
                <input
                  type="checkbox"
                  checked={form.locationIds.includes(loc.id)}
                  onChange={() => handleLocationToggle(loc.id)}
                />
                {loc.locationName} ({loc.zipCode})
              </label>
            ))}
            {allLocations.length === 0 && <span className="muted">No active locations available.</span>}
          </div>
        </div>

        <div className="form-actions">
          <button type="submit" className="btn" disabled={saving}>
            {saving ? 'Saving...' : 'Save'}
          </button>
          <button type="button" className="btn btn-secondary" onClick={() => navigate('/persons')}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}

import { apiRequest } from './client';

export function fetchLocations(activeOnly = false) {
  const qs = activeOnly ? '?activeOnly=true' : '';
  return apiRequest(`/locations${qs}`);
}

export function fetchLocation(id) {
  return apiRequest(`/locations/${id}`);
}

export function createLocation(data) {
  return apiRequest('/locations', { method: 'POST', body: data });
}

export function updateLocation(id, data) {
  return apiRequest(`/locations/${id}`, { method: 'PUT', body: data });
}

export function deleteLocation(id) {
  return apiRequest(`/locations/${id}`, { method: 'DELETE' });
}

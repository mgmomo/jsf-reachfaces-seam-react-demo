import { apiRequest } from './client';

export function fetchPersons() {
  return apiRequest('/persons');
}

export function fetchPerson(id) {
  return apiRequest(`/persons/${id}`);
}

export function createPerson(data) {
  return apiRequest('/persons', { method: 'POST', body: data });
}

export function updatePerson(id, data) {
  return apiRequest(`/persons/${id}`, { method: 'PUT', body: data });
}

export function deletePerson(id) {
  return apiRequest(`/persons/${id}`, { method: 'DELETE' });
}

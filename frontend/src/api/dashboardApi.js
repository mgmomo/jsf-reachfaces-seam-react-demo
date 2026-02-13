import { apiRequest } from './client';

export function fetchDashboard() {
  return apiRequest('/dashboard');
}

import { apiRequest } from './client';

export function fetchCurrentUser() {
  return apiRequest('/auth/me');
}

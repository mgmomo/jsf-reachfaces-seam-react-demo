const BASE = '/wision4-seam/api';

export async function apiRequest(path, options = {}) {
  const headers = { ...options.headers };

  const user = localStorage.getItem('auth_user');
  const role = localStorage.getItem('auth_role');
  if (user) {
    headers['X-Remote-User'] = user;
  }
  if (role) {
    headers['X-Remote-Roles'] = role;
  }

  if (options.body && typeof options.body === 'object') {
    headers['Content-Type'] = 'application/json';
    options.body = JSON.stringify(options.body);
  }

  const res = await fetch(`${BASE}${path}`, { ...options, headers });

  if (res.status === 204) {
    return null;
  }

  const data = await res.json();

  if (!res.ok) {
    const err = new Error(data.message || `HTTP ${res.status}`);
    err.status = res.status;
    throw err;
  }

  return data;
}

import { Link, Outlet } from 'react-router-dom';
import { useState } from 'react';
import { useAuth } from '../context/AuthContext';

const ROLES = ['ADMIN', 'USER', 'GUEST'];
const embedded = window.self !== window.top;

export default function Layout() {
  const { user, isAdmin, isUser, login, logout } = useAuth();
  const [rolePick, setRolePick] = useState(localStorage.getItem('auth_role') || 'GUEST');
  const [userPick, setUserPick] = useState(localStorage.getItem('auth_user') || '');

  function handleRoleSwitch(e) {
    e.preventDefault();
    login(userPick || rolePick.toLowerCase(), rolePick);
  }

  function handleLogout(e) {
    e.preventDefault();
    setRolePick('GUEST');
    setUserPick('');
    logout();
  }

  if (embedded) {
    return <Outlet />;
  }

  return (
    <div className="app">
      <header className="app-header">
        <div className="header-top">
          <h1 className="app-title">Vision4 React</h1>
          <div className="role-switcher">
            <input
              type="text"
              placeholder="username"
              value={userPick}
              onChange={(e) => setUserPick(e.target.value)}
              className="role-input"
            />
            <select value={rolePick} onChange={(e) => setRolePick(e.target.value)} className="role-select">
              {ROLES.map((r) => (
                <option key={r} value={r}>{r}</option>
              ))}
            </select>
            <button onClick={handleRoleSwitch} className="btn btn-sm">Switch</button>
            <button onClick={handleLogout} className="btn btn-sm btn-secondary">Logout</button>
            {user && (
              <span className="role-badge">
                {user.username} ({user.role})
              </span>
            )}
          </div>
        </div>
        <nav className="app-nav">
          <Link to="/">Home</Link>
          <Link to="/dashboard">Dashboard</Link>
          <Link to="/persons">Persons</Link>
          <Link to="/locations">Locations</Link>
          <a href="/vision4-seam/home.seam">Vision</a>
          <Link to="/about">About</Link>
        </nav>
      </header>

      <main className="app-main">
        <Outlet />
      </main>

      <footer className="app-footer">
        Vision4 Demo &mdash; Seam 2 + React
      </footer>
    </div>
  );
}

import { Link } from 'react-router-dom';

export default function AccessDeniedPage() {
  return (
    <div>
      <h2>Access Denied</h2>
      <p>You do not have sufficient permissions to access this page.</p>
      <p>Use the role switcher in the header to change your role.</p>
      <Link to="/" className="btn">Back to Home</Link>
    </div>
  );
}

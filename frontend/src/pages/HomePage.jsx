import { useAuth } from '../context/AuthContext';

export default function HomePage() {
  const { user, isAdmin, isUser } = useAuth();

  return (
    <div>
      <h2>Welcome to Wision4</h2>
      <p>This is the React frontend for the Wision4-Seam demo application.</p>

      {user && (
        <div className="info-box">
          <p><strong>Current user:</strong> {user.username}</p>
          <p><strong>Role:</strong> {user.role}</p>
          <p>
            <strong>Access:</strong>{' '}
            {isAdmin
              ? 'Full access (view, create, edit, delete)'
              : isUser
              ? 'Read-only access (view persons and locations)'
              : 'No data access (switch role using the header controls)'}
          </p>
        </div>
      )}
    </div>
  );
}

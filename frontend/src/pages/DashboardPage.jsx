import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { fetchDashboard } from '../api/dashboardApi';

const COLORS = ['#2b6cb0', '#e53e3e', '#38a169', '#d69e2e', '#805ad5', '#dd6b20', '#319795', '#b83280', '#2d3748', '#718096'];
const embedded = window.self !== window.top;
const CTX = '/vision4-seam';

function DashLink({ to, jsfPath, children }) {
  if (embedded) {
    return (
      <a
        href={`${CTX}${jsfPath}`}
        target="_top"
        onClick={(e) => {
          e.preventDefault();
          window.top.location.href = `${CTX}${jsfPath}`;
        }}
      >
        {children}
      </a>
    );
  }
  return <Link to={to}>{children}</Link>;
}

function PieChart({ data }) {
  if (!data || data.length === 0) return null;
  const total = data.reduce((sum, d) => sum + d.count, 0);
  if (total === 0) return <p className="muted">No locations yet</p>;

  const size = 160;
  const cx = size / 2;
  const cy = size / 2;
  const r = 60;
  let cumulative = 0;

  const slices = data.map((d, i) => {
    const fraction = d.count / total;
    const startAngle = cumulative * 2 * Math.PI - Math.PI / 2;
    cumulative += fraction;
    const endAngle = cumulative * 2 * Math.PI - Math.PI / 2;

    if (fraction >= 1) {
      return (
        <circle key={i} cx={cx} cy={cy} r={r} fill={COLORS[i % COLORS.length]} />
      );
    }

    const x1 = cx + r * Math.cos(startAngle);
    const y1 = cy + r * Math.sin(startAngle);
    const x2 = cx + r * Math.cos(endAngle);
    const y2 = cy + r * Math.sin(endAngle);
    const largeArc = fraction > 0.5 ? 1 : 0;

    return (
      <path
        key={i}
        d={`M${cx},${cy} L${x1},${y1} A${r},${r} 0 ${largeArc} 1 ${x2},${y2} Z`}
        fill={COLORS[i % COLORS.length]}
      />
    );
  });

  return (
    <div className="chart-with-legend">
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
        {slices}
      </svg>
      <div className="chart-legend">
        {data.map((d, i) => (
          <div key={i} className="legend-item">
            <span className="legend-color" style={{ background: COLORS[i % COLORS.length] }} />
            {d.label}: {d.count} ({total > 0 ? Math.round((d.count / total) * 100) : 0}%)
          </div>
        ))}
      </div>
    </div>
  );
}

function BarChart({ data }) {
  if (!data || data.length === 0) return <p className="muted">No data yet</p>;
  const max = Math.max(...data.map((d) => d.personCount));
  if (max === 0) return <p className="muted">No assignments yet</p>;

  return (
    <div className="bar-chart">
      {data.map((d, i) => (
        <div key={i} className="bar-row">
          <span className="bar-label" title={d.locationName}>
            {d.locationName.length > 18 ? d.locationName.substring(0, 16) + '...' : d.locationName}
          </span>
          <div className="bar-track">
            <div
              className="bar-fill"
              style={{
                width: `${(d.personCount / max) * 100}%`,
                background: COLORS[i % COLORS.length],
              }}
            />
          </div>
          <span className="bar-value">{d.personCount}</span>
        </div>
      ))}
    </div>
  );
}

export default function DashboardPage() {
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchDashboard()
      .then(setData)
      .catch((err) => setError(err.message));
  }, []);

  if (error) return <div className="error-msg">{error}</div>;
  if (!data) return <div className="loading">Loading dashboard...</div>;

  return (
    <div className="dashboard">
      <h2>Dashboard</h2>

      <div className="dashboard-cards">
        <div className="dash-card">
          <div className="dash-card-value">{data.totalPersons}</div>
          <div className="dash-card-label">Total People</div>
        </div>
        <div className="dash-card">
          <div className="dash-card-value">{data.totalLocations}</div>
          <div className="dash-card-label">Total Locations</div>
        </div>
        <div className="dash-card">
          <div className="dash-card-value">{data.activeLocations}</div>
          <div className="dash-card-label">Active Locations</div>
        </div>
        <div className="dash-card">
          <div className="dash-card-value">{data.inactiveLocations}</div>
          <div className="dash-card-label">Inactive Locations</div>
        </div>
        <div className="dash-card">
          <div className="dash-card-value">{data.personsWithNoLocations}</div>
          <div className="dash-card-label">Unassigned People</div>
        </div>
        <div className="dash-card">
          <div className="dash-card-value">{data.avgLocationsPerPerson}</div>
          <div className="dash-card-label">Avg Locations / Person</div>
        </div>
      </div>

      <div className="dashboard-section">
        <h3>Recent Items</h3>
        <div className="dashboard-recent">
          <div className="recent-panel">
            <h4>Recent People</h4>
            {data.recentPersons.length === 0 ? (
              <p className="muted">No people yet</p>
            ) : (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Date of Birth</th>
                    <th>Locations</th>
                  </tr>
                </thead>
                <tbody>
                  {data.recentPersons.map((p) => (
                    <tr key={p.id}>
                      <td>
                        <DashLink
                          to={`/persons/${p.id}/edit`}
                          jsfPath={`/personEdit.seam?personId=${p.id}`}
                        >
                          {p.firstName} {p.lastName}
                        </DashLink>
                      </td>
                      <td>{p.dateOfBirth || '-'}</td>
                      <td>{p.locations ? p.locations.length : 0}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
          <div className="recent-panel">
            <h4>Recent Locations</h4>
            {data.recentLocations.length === 0 ? (
              <p className="muted">No locations yet</p>
            ) : (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>ZIP</th>
                    <th>State</th>
                  </tr>
                </thead>
                <tbody>
                  {data.recentLocations.map((l) => (
                    <tr key={l.id}>
                      <td>
                        <DashLink
                          to={`/locations/${l.id}/edit`}
                          jsfPath={`/locationEdit.seam?locationId=${l.id}`}
                        >
                          {l.locationName}
                        </DashLink>
                      </td>
                      <td>{l.zipCode || '-'}</td>
                      <td>{l.stateLabel}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>

      <div className="dashboard-section">
        <h3>Charts</h3>
        <div className="dashboard-charts">
          <div className="chart-panel">
            <h4>Location Status</h4>
            <PieChart data={data.locationStateDistribution} />
          </div>
          <div className="chart-panel">
            <h4>Top Locations by Person Count</h4>
            <BarChart data={data.personsPerLocation} />
          </div>
        </div>
      </div>
    </div>
  );
}

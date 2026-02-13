export default function AboutPage() {
  return (
    <div>
      <h2>About Vision4 Seam Demo</h2>

      <p>
        This application is a demonstration of a small legacy Java EE application.
        It simulates a typical enterprise application from the JBoss Seam era,
        using the component-based development model that was prevalent in the late 2000s.
      </p>

      <div className="info-box" style={{ marginTop: '1rem' }}>
        <h3>Technology Stack</h3>
        <dl>
          <dt><strong>Application Server</strong></dt>
          <dd>JBoss AS 7.1.1.Final</dd>
          <dt><strong>Web Framework</strong></dt>
          <dd>JBoss Seam 2.2.2.Final</dd>
          <dt><strong>UI Component Library</strong></dt>
          <dd>RichFaces 3.3.4.Final</dd>
          <dt><strong>View Technology</strong></dt>
          <dd>JavaServer Faces (JSF) 1.2 with Facelets</dd>
          <dt><strong>Persistence</strong></dt>
          <dd>JPA 2.0 with Hibernate 4 (provided by JBoss AS 7)</dd>
          <dt><strong>Database</strong></dt>
          <dd>H2 (embedded, in-memory)</dd>
          <dt><strong>Build Tool</strong></dt>
          <dd>Apache Maven</dd>
          <dt><strong>React Frontend</strong></dt>
          <dd>React 19, React Router, Vite</dd>
        </dl>
      </div>

      <div className="info-box" style={{ marginTop: '1rem' }}>
        <h3>Application Features</h3>
        <ul style={{ marginLeft: '1.5rem', marginTop: '0.5rem' }}>
          <li><strong>Person Management</strong> — Create, edit, and delete people with first name, last name, and date of birth.</li>
          <li><strong>Location Management</strong> — Create, edit, and delete locations with name, address, zip code, and active/not active status.</li>
          <li><strong>Person-Location Assignment</strong> — Assign one or more locations to a person using a many-to-many relationship.</li>
        </ul>
      </div>

      <div className="info-box" style={{ marginTop: '1rem' }}>
        <h3>Application Information</h3>
        <dl>
          <dt><strong>Version</strong></dt>
          <dd>1.0.0-SNAPSHOT</dd>
        </dl>
      </div>
    </div>
  );
}

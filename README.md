# JSF / RichFaces / Seam + React Demo

A demo application showing a legacy Java EE stack (JBoss Seam 2, RichFaces 3, JSF 1.2) running alongside a modern React SPA, both served from the same WAR on JBoss AS 7.1.1.

## Technology Stack

**Legacy (JSF):**
- JBoss AS 7.1.1.Final
- JBoss Seam 2.2.2.Final
- RichFaces 3.3.4.Final
- JSF 1.2 (Facelets 1.1)
- JPA 2.0 / Hibernate
- H2 in-memory database

**Modern (React):**
- React 19
- React Router (HashRouter)
- Vite

## Features

- **Person Management** -- Create, edit, delete persons with first name, last name, and date of birth
- **Location Management** -- Create, edit, delete locations with name, address, zip code, and active/inactive status
- **Person-Location Assignment** -- Many-to-many relationship between persons and locations
- **Dashboard** -- Summary statistics, recent items, and charts (pie chart for location status, bar chart for top locations by person count)
- **Dual UI** -- Every feature available in both the JSF/RichFaces UI and the React SPA
- **Integrated Navigation** -- JSF menu links to embedded React pages (via iframe); React nav links back to JSF app
- **Header-Based Auth** -- Role simulation via `X-Remote-User` / `X-Remote-Roles` HTTP headers (ADMIN, USER, GUEST)

## Quick Start

### Prerequisites

- Java 17+ (for Maven compilation)
- Maven 3.x
- Node.js 18+

### Setup

The `local/` directory must contain the JBoss AS 7.1.1 runtime and Zulu Java 7:

```
local/
  jboss-as-7.1.1.Final/
  zulu7.56.0.11-ca-jdk7.0.352-linux_x64/
```

### Run

```bash
# Terminal 1: Start JBoss
./start-jboss.sh

# Terminal 2: Build and deploy
./build-deploy.sh
```

### Access

- **JSF app:** http://localhost:8180/vision4-seam/home.seam
- **React app:** http://localhost:8180/vision4-seam/app/

## Project Structure

```
start-jboss.sh           # Start local JBoss AS 7.1.1
build-deploy.sh          # Build frontend + WAR and deploy

frontend/                # React SPA (Vite + React 19)
  src/
    api/                 # REST API client
    components/          # Layout, ProtectedRoute
    pages/               # Page components
    context/             # Auth context
  vite.config.js

src/main/java/com/vision/demo/
  model/                 # JPA entities: Person, Location
  action/                # Seam POJO action components
  service/               # @Stateless EJB: DataService
  rest/                  # JAX-RS REST resources

src/main/webapp/
  layout/template.xhtml  # Facelets master template
  *.xhtml                # JSF pages
  personReact.xhtml      # Embedded React persons (iframe)
  locationReact.xhtml    # Embedded React locations (iframe)
  dashboardReact.xhtml   # Embedded React dashboard (iframe)
  css/style.css

local/                   # Runtime environment (not in git)
```

## Architecture

The application demonstrates integrating a React SPA into an existing legacy Java EE application:

1. **React pages embedded in JSF** -- JSF wrapper pages (`personReact.xhtml`, `locationReact.xhtml`, `dashboardReact.xhtml`) use the Facelets template and embed the React app via iframe. The React `Layout` component detects iframe embedding and hides its own chrome. Embedded dashboard links navigate the parent frame to JSF pages rather than React routes.
2. **Shared REST API** -- JAX-RS endpoints under `/api/` serve both the React frontend and can be used independently.
3. **Bundled in one WAR** -- The React build output (`frontend/dist/`) is included in the WAR under `/app/` via Maven's `webResources` configuration.
4. **HashRouter** -- React uses `HashRouter` so all routes are hash fragments. JBoss only needs to serve the static `index.html`.

## License

MIT

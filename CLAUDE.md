# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Wision4-seam is a legacy demo application simulating a small enterprise Java EE system. It manages persons and locations with a many-to-many relationship between them.

**Stack:** JBoss Seam 2.2.2.Final, RichFaces 3.3.4.Final, JSF 1.2 (Facelets 1.1), JPA 2.0, Hibernate 3.3.0.SP1 (bundled), deployed on JBoss AS 7.1.1.

## Build and Deploy

```bash
# Build the WAR (requires Maven 3.x; runs on Java 17 for compilation, targets Java 7 bytecode)
mvn clean package

# Deploy to JBoss AS 7.1.1 (copy WAR to deployments folder)
cp target/wision4-seam.war $JBOSS_HOME/standalone/deployments/

# Start JBoss AS 7 with port offset (avoids conflict with other instances)
$JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -Djboss.socket.binding.port-offset=100
```

The application is accessible at `http://localhost:8180/wision4-seam/home.seam` (port 8180 with offset 100).

Uses the built-in H2 in-memory datasource (`java:jboss/datasources/ExampleDS`). Schema is auto-created on deploy (`hibernate.hbm2ddl.auto=create-drop`). Seed data loaded from `src/main/resources/import.sql`.

### JBoss AS 7.1.1 Installation

JBoss AS 7.1.1 is installed at `/home/gerald/jboss-as-7.1.1.Final/` with these patches applied:
- `jboss-modules.jar` replaced with 1.1.5.GA (fixes `__redirected.__SAXParserFactory` NPE on modern JVMs)
- `standalone.conf` has JAXP workaround system properties and JAVA_HOME pointing to Zulu Java 7
- Runs with port offset 100 to coexist with the wision3 instance on port 8080

## Architecture

Two-layer architecture: **Seam POJO action components** (UI/conversation logic) + **@Stateless EJB DataService** (persistence).

### Key Patterns

- **Action components** are Seam POJOs (`@Name`, `@Scope`) — not CDI beans, not EJBs
- **DataService** is a `@Stateless` EJB with `@PersistenceContext` — the only bean that touches the EntityManager
- Action components obtain DataService via **manual JNDI lookup**: `new InitialContext().lookup("java:module/DataService")`
- This pattern avoids Seam 2's incompatibility with JBoss AS 7's EJB proxy mechanism (Seam `@In` injection of EJBs causes "value of context variable is not an instance of the component" errors)
- **Conversation scope** (`@Begin`/`@End`) on edit actions maintains state across AJAX requests (e.g., adding/removing locations before saving)
- **Page actions** in `pages.xml` call `init()` methods when pages load, with request params bound to component fields
- Navigation uses Seam's `<s:link>` with `propagation="none"` in menus to avoid leaking conversations
- URL pattern is `*.seam` (mapped in `web.xml`)
- JSF 1.2 `f:selectItems` does NOT support `var`/`itemLabel`/`itemValue` attributes — action beans must return `List<SelectItem>` instead
- Lazy-loaded collections (e.g., `Person.locations`) must be fetched eagerly via `JOIN FETCH` in JPQL queries since the persistence context closes when DataService methods return

### Source Layout

```
src/main/java/com/wision/demo/
  model/          # JPA entities: Person, Location, LocationState enum
  action/         # Seam POJO action components: PersonAction, LocationAction, *ListAction
  service/        # @Stateless EJB: DataService (all persistence operations)

src/main/webapp/
  layout/template.xhtml    # Facelets master template (header, menu, footer)
  home.xhtml               # Landing page
  personEdit.xhtml         # Create/edit person with location assignment
  personList.xhtml         # Person list with rich:dataTable
  locationEdit.xhtml       # Create/edit location
  locationList.xhtml       # Location list with rich:dataTable
  about.xhtml              # Application info page
  css/style.css            # All application styles

WEB-INF/
  components.xml                # Seam config (jndi-pattern, transaction, conversation)
  pages.xml                     # Page actions and parameter bindings
  faces-config.xml              # Facelets ViewHandler registration
  web.xml                       # Servlets, filters, RichFaces skin config
  jboss-deployment-structure.xml # Excludes JBoss AS 7 built-in JSF 2.0 modules
```

### Entity Relationship

`Person` ←ManyToMany→ `Location` via `person_location` join table. A person can be assigned multiple active locations. The `LocationState` enum (`ACTIVE`/`NOT_ACTIVE`) controls location availability.

## JBoss AS 7 Compatibility

Seam 2.2.2 was designed for JBoss AS 4/5/6. Running on JBoss AS 7.1.1 requires:

- `jboss-deployment-structure.xml` excludes the built-in JSF 2.0 modules so bundled JSF 1.2 is used (schema 1.0 only — `exclude-subsystems` is not supported in AS 7.1.1)
- JSF 1.2 RI (Mojarra), Facelets 1.1, and RichFaces 3.3.4 are bundled in the WAR
- Hibernate 3.3.0.SP1 bundled in WAR for Seam 2 proxy compatibility (JBoss AS 7 ships Hibernate 4 which breaks Seam's `HibernateSessionProxy`)
- `jboss-seam-jul.jar` excluded via maven-war-plugin `packagingExcludes` to avoid StackOverflowError from logging recursion
- Maven repositories `jboss-public` and `jboss-deprecated` are required for resolving legacy artifacts
- Maven plugin versions must be 3.x+ compatible (e.g., maven-war-plugin 3.4.0, maven-compiler-plugin 3.11.0) since Maven runs on Java 17

## RichFaces / JSF Notes

- Tag namespaces: `a4j` = `http://richfaces.org/a4j`, `rich` = `http://richfaces.org/rich`, `s` = `http://jboss.com/products/seam/taglib`
- RichFaces 3.x uses `reRender` (camelCase), not `render` as in RF 4.x
- AJAX updates use `<a4j:commandButton>` with `reRender` pointing to component IDs
- `<rich:calendar>` provides date picker; `<rich:dataTable>` provides sortable/pageable tables
- JSF 1.2 has no `<h:head>`/`<h:body>` — use plain HTML `<head>`/`<body>` inside `<f:view>`

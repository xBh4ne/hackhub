# HackHub

HackHub is the REST backend for the complete management of hackathons: account registration and authentication, hackathon creation and administration, team formation, invitations, submissions, evaluations, winner proclamation, mentor support requests, and team reports.

The project is a Java application built on Spring Boot and organized into layers inspired by Domain-Driven Design. No web interface is included: all functionality is exposed through HTTP APIs and documented in the Postman collection included in the repository.

## Table of Contents

- [Key Features](#key-features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Requirements](#requirements)
- [Running the Application](#running-the-application)
- [Configuration](#configuration)
- [Authentication and Authorization](#authentication-and-authorization)
- [Roles](#roles)
- [Hackathon Lifecycle](#hackathon-lifecycle)
- [Invitation Lifecycle](#invitation-lifecycle)
- [API Reference](#api-reference)
- [Usage Examples](#usage-examples)
- [Project Structure](#project-structure)
- [Postman](#postman)
- [Project Status and Known Limitations](#project-status-and-known-limitations)

## Key Features

- Registration and login with JWT issuance.
- Management of user accounts and staff accounts with distinct roles.
- Hackathon creation and configuration by organizers.
- Opening registrations, starting the hackathon, starting and completing evaluations, and proclaiming the winner.
- Team creation, invitation management, and team join/leave operations.
- Team registration for hackathons.
- Submission creation and updates by teams.
- Submission evaluation by judges.
- Support requests to mentors and call booking.
- Team reports by mentors and report review by organizers.
- Centralized error handling through structured JSON responses.

## Technology Stack

| Component | Technology |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Web | Spring MVC |
| Persistence | Spring Data JPA, Hibernate |
| Database | H2 (local file) |
| Security | Spring Security, JWT (JJWT 0.12.6) |
| Validation | Jakarta Bean Validation |
| Build | Maven Wrapper |
| Utilities | Lombok |

## Architecture

The codebase is divided into three main areas.

### Domain

Contains the business model and application rules:

- JPA entities (`Hackathon`, `Team`, `Utente`, `Staff`, `Invito`, `Sottomissione`, `Valutazione`, `RichiestaSupporto`, `Segnalazione`, `Call`);
- state machines for hackathons and invitations, implemented with the State pattern;
- domain services;
- application facades that orchestrate services, validators, and mappers;
- role and membership validators;
- repository interfaces and ports to external services (`CalendarAdapter`, `PagamentoAdapter`).

### Infrastructure

Contains the technical implementations:

- Spring Security, PasswordEncoder, and AuthenticationProvider configuration;
- JWT filter and token provider;
- JPA converters for state objects;
- adapter implementations;
- global and domain-specific exception handling;
- development data initialization.

### Presentation

Exposes the REST API:

- controllers grouped by functional area;
- request and response DTOs;
- mappers between domain entities and DTOs.

## Requirements

- JDK 21 installed and configured in `JAVA_HOME`.
- No separate Maven installation is required: the repository includes the Maven Wrapper.
- Port `8080` available, unless configured otherwise.

## Running the Application

Clone the repository and run the application from the project root:

```bash
./mvnw spring-boot:run
```

Alternatively, build the executable package:

```bash
./mvnw clean package
java -jar target/hack-hub-1.jar
```

The application listens on `http://localhost:8080` by default.

On first startup, a development administrator account is created automatically:

| Username | Password | Role |
| --- | --- | --- |
| `admin` | `admin` | `ADMIN` |

The account is created only if it does not already exist. These credentials are intended for development only and must be removed or changed before any real-world use.

## Configuration

Configuration is located in `src/main/resources/application.properties`.

| Property | Default Value | Description |
| --- | --- | --- |
| `spring.datasource.url` | `jdbc:h2:file:./data/hackhub;AUTO_SERVER=TRUE` | Local file-based H2 database |
| `spring.jpa.hibernate.ddl-auto` | `update` | Automatic schema updates |
| `jwt.expiration` | `900` | Token lifetime in seconds |
| `jwt.secret` | development value | Token signing key |
| `jwt.issuer` | `hack-hub` | Token issuer |
| `jwt.audience` | `hack-hub-client` | Token audience |
| `security.salt` | development value | Password encoding salt |

The values stored in the repository are intended for local development. For shared or production environments, at least the database credentials, salt, and JWT key must be externalized, for example through environment variables (`JWT_SECRET`, `JWT_EXPIRATION`, `SECURITY_SALT`, `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`).

The database is created in the `data/` directory on first startup. Deleting that directory results in the loss of all stored data.

## Authentication and Authorization

Authentication is stateless and based on JWT.

1. The user logs in via `POST /api/auth/login`.
2. The server returns an object containing `accessToken`, `expiration`, and `type`.
3. The token must be sent with protected requests using the following header:

```http
Authorization: Bearer <accessToken>
```

Passwords are hashed with PBKDF2-HMAC-SHA256. Authorization rules are enforced through `@PreAuthorize` annotations on controllers, while the JWT filter populates the Spring Security context.

Registration and login endpoints, the hackathon list, hackathon details, and the winning team are accessible without authentication. All other operations require a valid token and the appropriate role.

## Roles

### User Roles

| Role | Description |
| --- | --- |
| `UTENTE` | Standard account. Can create a team, invite members, register for hackathons, submit work, and request support. |
| `TEAM_MEMBER` | Role defined in the model for team members. |
| `TEAM_LEADER` | Role defined in the model for team leaders. |

### Staff Roles

| Role | Description |
| --- | --- |
| `ADMIN` | Administers the platform, creates staff accounts, and views all accounts, teams, and reports. |
| `ORGANIZZATORE` | Creates and manages hackathons, assigns mentors, opens registrations, starts phases, and proclaims the winner. |
| `GIUDICE` | Evaluates submissions for assigned hackathons and completes the evaluation phase. |
| `MENTORE` | Handles team support requests, books calls, and submits reports. |

## Hackathon Lifecycle

```text
CREATO
  -> ISCRIZIONE
  -> IN_CORSO
  -> VALUTAZIONE
  -> PROCLAMAZIONE_VINCITORE
  -> CONCLUSO
```

| State | Allowed Operations |
| --- | --- |
| `CREATO` | Open registrations |
| `ISCRIZIONE` | Team registration, hackathon start |
| `IN_CORSO` | Submission upload, evaluation start |
| `VALUTAZIONE` | Submission evaluation, evaluation completion |
| `PROCLAMAZIONE_VINCITORE` | Winner proclamation |
| `CONCLUSO` | No further transitions |

Completing the evaluation requires all submissions to have been evaluated. Proclaiming the winner requires all submissions to have been evaluated and the operation to be performed by the hackathon organizer.

## Invitation Lifecycle

```text
PENDENTE
  -> ACCETTATO
  -> RIFIUTATO
```

An accepted or rejected invitation cannot change state again. Acceptance is allowed only for a user who is not already part of a team. Two invitations for the same team-user pair cannot be created.

## API Reference

All paths are relative to `http://localhost:8080`.

### Authentication and Accounts

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `POST` | `/api/auth/login` | Public | Authenticates a user and returns a token |
| `POST` | `/api/auth/register` | Public | Registers a new user with the `UTENTE` role |
| `POST` | `/api/auth/register/staff` | `ADMIN` | Creates a staff account with the specified role |
| `GET` | `/api/auth/accounts` | `ADMIN` | Lists all user and staff accounts |

### User

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `GET` | `/api/utente` | `UTENTE` | Returns the authenticated user's profile |
| `GET` | `/api/utente/inviti` | `UTENTE` | Lists pending invitations received by the user |

### Hackathons

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `GET` | `/api/hackathons` | Public | Lists hackathons |
| `GET` | `/api/hackathons/{hackathonId}` | Public | Returns hackathon details |
| `POST` | `/api/hackathons` | `ORGANIZZATORE` | Creates a hackathon |
| `POST` | `/api/hackathons/{hackathonId}/mentore/add/{mentoreId}` | `ORGANIZZATORE` | Assigns a mentor to the hackathon |
| `POST` | `/api/hackathons/{hackathonId}/mentori/remove/{mentoreId}` | `ORGANIZZATORE` | Removes a mentor from the hackathon |
| `POST` | `/api/hackathons/{hackathonId}/apri-iscrizioni` | `ORGANIZZATORE` | Opens registrations |
| `POST` | `/api/hackathons/{hackathonId}/iscrivi` | `UTENTE`, `TEAM_LEADER` | Registers the user's team |
| `POST` | `/api/hackathons/{hackathonId}/avvia` | `ORGANIZZATORE` | Starts the hackathon |
| `POST` | `/api/hackathons/{hackathonId}/avvia-valutazione` | `ORGANIZZATORE` | Starts the evaluation phase |
| `POST` | `/api/hackathons/{hackathonId}/concludi-valutazione` | `GIUDICE` | Completes the evaluation phase |
| `POST` | `/api/hackathons/{hackathonId}/proclama-vincitore` | `ORGANIZZATORE` | Proclaims the winning team |
| `GET` | `/api/hackathons/{hackathonId}/team-vincitore` | Public | Returns the winning team |

### Teams

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `GET` | `/api/teams` | `ADMIN` | Lists all teams |
| `POST` | `/api/teams` | `UTENTE` | Creates a team |
| `POST` | `/api/teams/{hackathonId}/paga-vincitore` | `ORGANIZZATORE` | Initiates payment of the prize |
| `GET` | `/api/team` | `UTENTE` | Returns the authenticated user's team |
| `POST` | `/api/team/esci` | `UTENTE` | Leaves the team; if the user is the last member, the team is deleted |
| `GET` | `/api/team/inviti` | `UTENTE` | Lists pending invitations sent by the team |
| `GET` | `/api/team/hackathons` | `UTENTE` | Lists hackathons the team is registered for |
| `GET` | `/api/team/sottomissioni` | `UTENTE` | Lists the team's submissions |
| `GET` | `/api/team/richieste-supporto` | `UTENTE` | Lists the team's support requests |

### Invitations

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `GET` | `/api/inviti` | `ADMIN`, `UTENTE` | Lists pending invitations; users see only their own |
| `POST` | `/api/inviti` | `UTENTE` | Invites a user to the current team |
| `POST` | `/api/inviti/{invitoId}/accetta` | `UTENTE` | Accepts an invitation |
| `POST` | `/api/inviti/{invitoId}/rifiuta` | `UTENTE` | Rejects an invitation |

### Submissions and Evaluations

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `GET` | `/api/sottomissioni/{sottomissioneId}` | `GIUDICE`, `ORGANIZZATORE`, `UTENTE` | Returns a submission |
| `POST` | `/api/sottomissioni/send` | `UTENTE` | Submits new work |
| `POST` | `/api/sottomissioni/update` | `UTENTE` | Updates the team's submission |
| `POST` | `/api/sottomissioni/{sottomissioneId}/valuta` | `GIUDICE` | Evaluates or updates the evaluation of a submission |

### Staff

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `GET` | `/api/staffs` | `ADMIN` | Lists all staff accounts |
| `GET` | `/api/staffs/admins` | `ADMIN` | Lists administrators |
| `GET` | `/api/staffs/giudici` | `ADMIN`, `ORGANIZZATORE` | Lists judges |
| `GET` | `/api/staffs/organizzatori` | `ADMIN` | Lists organizers |
| `GET` | `/api/staffs/mentori` | `ADMIN`, `ORGANIZZATORE` | Lists mentors |
| `GET` | `/api/staffs/hackathons` | `GIUDICE`, `ORGANIZZATORE`, `MENTORE` | Lists hackathons assigned to the staff member |
| `GET` | `/api/staffs/sottomissioni` | `GIUDICE` | Lists submissions to be evaluated |

### Support and Reports

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `GET` | `/api/richieste-supporto` | `MENTORE` | Lists support requests assigned to the mentor |
| `POST` | `/api/richieste-supporto/hackathon/{hackathonId}/mentore/{mentoreId}` | `UTENTE` | Creates a support request |
| `POST` | `/api/richieste-supporto/{richiestaId}/call` | `MENTORE` | Books a call for a support request |
| `GET` | `/api/segnalazioni` | `ORGANIZZATORE`, `ADMIN` | Lists manageable reports |
| `POST` | `/api/segnalazioni/hackathon/{hackathonId}/team/{teamId}` | `MENTORE` | Reports a team |
| `GET` | `/api/segnalazioni/team/{teamId}` | `ORGANIZZATORE` | Lists reports for a team |

### Error Format

Application errors are returned with a consistent JSON structure:

```json
{
  "code": 400,
  "status": "Bad Request",
  "message": "Error description",
  "timestamp": "27/10/2026 14:30:00"
}
```

HTTP status codes used include `400`, `401`, `403`, `404`, `405`, and `409`.

## Usage Examples

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

### User Registration

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"mario.rossi","email":"mario.rossi@example.com","password":"password"}'
```

### Team Creation

```bash
curl -X POST http://localhost:8080/api/teams \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <accessToken>" \
  -d '{"name":"Team Alpha","descrizione":"Development team"}'
```

### Hackathon Creation

```bash
curl -X POST http://localhost:8080/api/hackathons \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <accessToken>" \
  -d '{
    "nome":"HackHub 2026",
    "descrizione":"Demo hackathon",
    "regolamento":"Event rules",
    "dataInizio":"02/05/2026 09:30",
    "dataFine":"01/09/2026 09:30",
    "dataScadenzaIscrizioni":"01/05/2026 09:00",
    "luogo":"Camerino",
    "premioDenaro":1000,
    "dimMaxTeam":10,
    "giudiceId":1
  }'
```

Dates are accepted in the `dd/MM/yyyy HH:mm` format.

## Project Structure

```text
hackhub/
├── src/main/java/unicam/dreamteam/
│   ├── domain/
│   │   ├── adapter/          Ports to external services
│   │   ├── exception/        Domain exceptions
│   │   ├── model/            Entities, builders, and state machines
│   │   ├── repository/       Spring Data JPA interfaces
│   │   ├── service/          Domain services and facades
│   │   └── validator/        Application validators
│   ├── infrastructure/
│   │   ├── adapter/          Adapter implementations
│   │   ├── api/              Response structures
│   │   ├── config/           Application and security configuration
│   │   ├── exception/        HTTP exception handling
│   │   └── persistence/      JPA converters
│   └── presentation/
│       ├── api/controllers/  REST controllers
│       ├── dto/              Request and response DTOs
│       └── mapper/           Domain-to-DTO mapping
├── src/main/resources/
│   └── application.properties
├── hack-hub.postman_collection.json
├── hack-hub.vpp
├── pom.xml
├── mvnw
└── mvnw.cmd
```

The `hack-hub.vpp` file contains the Visual Paradigm project with the analysis and design models.

## Postman

The `hack-hub.postman_collection.json` collection contains requests for all roles and the main application workflows.

To use it:

1. import the file into Postman;
2. set the collection variables;
3. log in to obtain a token;
4. assign the token to the `ACCESS_TOKEN` variable.

Recommended values for a local instance:

| Variable | Value |
| --- | --- |
| `BASE_URL` | `http://localhost:8080/api` |
| `BASE_AUTH_URL` | `http://localhost:8080/api/auth` |
| `LOGIN_URL` | `http://localhost:8080/api/auth/login` |
| `REGISTER_UTENTE_URL` | `http://localhost:8080/api/auth/register` |

The username and password variables for each role must be set to accounts created in the system.

## Project Status and Known Limitations

- The integrations with the call booking system (`CalendarAdapter`) and the payment system (`PagamentoAdapter`) are placeholder implementations that throw an exception when invoked. Endpoints that depend on them are therefore not operational end-to-end.
- The `TEAM_MEMBER` and `TEAM_LEADER` roles are defined in the model, but creating a team does not change the user's role, which remains `UTENTE`.
- The maximum team size (`dimMaxTeam`) is stored on the hackathon but is not enforced when inviting or accepting members.
- No automated test suite is currently included in the repository.
- The H2 database with `ddl-auto=update` is suitable for development and demonstration, not for production.
- The credentials and keys in `application.properties` are development values and must be replaced.
- Some endpoints accept or return plain text, such as team reporting and prize payment.

## License

The repository currently does not contain a license file. Any use, distribution, or modification must be agreed with the project authors.

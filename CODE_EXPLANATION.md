# Code Explanation Document

This document explains how the project code is structured and how data flows through the system.

## 1. System Overview

The project has three layers:

1. `employment-service` (Spring Boot backend)
2. `api-gateway` (Express gateway)
3. `frontend` (React app)

High-level flow:

```text
Browser (React)
   -> API Gateway (Node/Express)
   -> Employment Service (Spring Boot)
   -> H2 DB + Mock Adapters (jobs/courses)
```

## 2. Backend (`employment-service`)

### 2.1 Entry Point

- `src/main/java/com/employment/service/EmploymentServiceApplication.java`
- Starts the Spring Boot app on port `8080`.

### 2.2 REST Controllers

The controller layer exposes API endpoints under `/api`.

1. `JobSeekerController`
- Base path: `/api/jobseekers`
- Endpoints:
  - `POST /register` -> create user
  - `GET /` -> list users
  - `GET /{userId}` -> get user
  - `PUT /{userId}/profile` -> create/update profile
  - `GET /{userId}/profile` -> read profile
  - `GET /{userId}/match` -> match jobs + recommend courses

2. `JobController`
- Base path: `/api/jobs`
- Endpoints:
  - `GET /` -> search jobs by `location` and/or `keyword`
  - `GET /{jobId}` -> get job details

3. `CourseController`
- Base path: `/api/courses`
- Endpoints:
  - `GET /?skills=...` -> fetch courses for skill list
  - `GET /{courseId}` -> get course details

### 2.3 Business Logic Service

`EmploymentService` is the core class that implements all use cases.

Main responsibilities:

- Register user (with duplicate email check)
- Get users and profiles
- Upsert profile (update or create)
- Search jobs via ODP adapter
- Fetch course details via course adapter
- Match jobs against user skills and compute match score

Matching algorithm summary:

1. Load user profile and skills
2. Fetch jobs for user location
3. For each job:
  - Find required skills
  - Calculate `missingSkills`
  - Compute match score: `matchedSkills / requiredSkills * 100`
  - Fetch recommended courses for missing skills
4. Sort results by descending score

### 2.4 Persistence Layer

- `JobSeekerRepository` extends `JpaRepository<JobSeeker, Long>`
- `ProfileRepository` extends `JpaRepository<Profile, Long>`

Entities:

- `JobSeeker` (`job_seekers` table)
  - `userId`, `name`, `email`, `password`, `location`
- `Profile` (`profiles` table)
  - linked 1:1 with `JobSeeker`
  - includes skill list (`@ElementCollection`) and preferences

### 2.5 DTOs and Validation

- `RegisterRequest`
  - requires name, email, password (min 8), location
- `ProfileRequest`
  - requires non-empty skills list and location

### 2.6 Adapter Layer (Mock external integrations)

1. `MockODPAdapter`
- In-memory job list
- Implements search and job detail APIs

2. `MockCourseAPIAdapter`
- In-memory course list
- Returns courses by skill and by ID

These adapters simulate external systems and are injected via interfaces.

### 2.7 Configuration

`src/main/resources/application.properties`

- Runs on port `8080`
- H2 in-memory DB (`create-drop`)
- Swagger/OpenAPI enabled:
  - `/api-docs`
  - `/swagger-ui.html`

## 3. API Gateway (`api-gateway`)

Main file: `api-gateway/server.js`

Responsibilities:

1. CORS + request logging (`morgan`)
2. Health endpoint:
- `GET /health`

3. Login endpoint:
- `POST /auth/login`
- Reads users from backend (`GET /api/jobseekers`)
- Validates email + password
- Returns a simplified user object for frontend session use

4. API proxy:
- Proxies `/api/*` to Spring backend
- `pathRewrite` keeps `/api` prefix for backend routing
- Handles proxy failures with `502 Bad Gateway`

Why gateway exists:
- Keep frontend talking to one service URL
- Centralize login behavior and proxy behavior
- Easier to evolve into microservices later

## 4. Frontend (`frontend`)

### 4.1 App Entry and Routing

- `src/main.jsx` mounts React app with `BrowserRouter`
- `src/App.jsx` defines routes:
  - `/` home
  - `/register`
  - `/login`
  - `/dashboard` (protected)
  - `/profile` (protected)

### 4.2 Auth and API Helpers

1. `src/lib/config.js`
- Gateway base URL (`VITE_GATEWAY_URL` or default `http://localhost:8081`)

2. `src/lib/api.js`
- Generic fetch wrapper
- Parses JSON and throws readable errors

3. `src/lib/auth.js`
- Stores current user in `localStorage`
- `getCurrentUser`, `setCurrentUser`, `clearCurrentUser`

4. `src/components/RequireAuth.jsx`
- Redirects unauthenticated users to `/login`

### 4.3 Pages

1. `RegisterPage`
- Calls `POST /api/jobseekers/register`
- Shows created user ID and redirects to login

2. `LoginPage`
- Calls `POST /auth/login`
- Stores user session in local storage

3. `DashboardPage`
- Job search (`GET /api/jobs`)
- Match results (`GET /api/jobseekers/{userId}/match`)
- Displays recommended courses with provider links

4. `ProfilePage`
- Shows account info fields (user ID, name, email)
- Loads existing profile (`GET /api/jobseekers/{userId}/profile`)
- Updates profile (`PUT /api/jobseekers/{userId}/profile`)

5. `HomePage`
- Simple route index with navigation links

### 4.4 UI Layout Components

- `AuthLayout` for login/register screens
- `AppShell` for authenticated pages (header + nav + logout)
- Styling in `src/index.css` (black/white/gray theme)

## 5. End-to-End Request Flows

### 5.1 Registration

1. User fills `/register`
2. Frontend -> Gateway: `POST /api/jobseekers/register`
3. Gateway proxies to backend
4. Backend validates and stores user
5. Frontend shows success + user ID

### 5.2 Login

1. User fills `/login`
2. Frontend -> Gateway: `POST /auth/login`
3. Gateway reads backend users and validates password
4. Frontend stores returned user in local storage

### 5.3 Profile Update

1. User opens `/profile`
2. Frontend loads profile from backend
3. User edits skills/preferences/location
4. Frontend sends update via gateway
5. Backend upserts profile and updates location

### 5.4 Match + Courses

1. User clicks “Load Matches” on `/dashboard`
2. Backend matches jobs based on profile skills
3. Backend returns `matchScore`, `missingSkills`, and recommended courses
4. Frontend renders results and course links

## 6. Important Technical Notes

1. Passwords are currently stored and compared in plain text (demo-level only).
2. Login is gateway-based and does not use JWT/session tokens.
3. External job/course integrations are mocked with in-memory adapter data.
4. H2 is in-memory, so data resets when backend restarts.

## 7. Suggested Next Improvements

1. Add secure password hashing (e.g., BCrypt) in backend.
2. Replace `/auth/login` with backend-auth + JWT.
3. Add unit/integration tests for service and gateway.
4. Replace mock adapters with real external API clients.
5. Add role-based access and proper error response contracts.

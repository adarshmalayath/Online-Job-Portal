# Employment Service Project

Full-stack setup for a local employment and skills matching system.

## Tech Stack

- `employment-service`: Java 17 + Spring Boot (REST backend)
- `api-gateway`: Node.js + Express (gateway and login endpoint)
- `frontend`: React + Vite (UI)

## Project Structure

```text
employment-service/   # Java backend
api-gateway/          # API gateway (proxy + login)
frontend/             # React frontend
```

## Prerequisites

- Java 17+
- Maven 3.9+
- Node.js 18+
- npm 9+

## Quick Start

Run each service in a separate terminal.

### 1) Start Backend

```bash
cd employment-service
mvn spring-boot:run
```

Backend runs on: `http://localhost:8080`

### 2) Start API Gateway

```bash
cd api-gateway
cp .env.example .env
npm install
npm run dev
```

Gateway runs on: `http://localhost:8081`

### 3) Start Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on: `http://localhost:5173`

## Frontend Routes

- `http://localhost:5173/register`
- `http://localhost:5173/login`
- `http://localhost:5173/dashboard`
- `http://localhost:5173/profile`

## Gateway Endpoints

- `GET /health`
- `POST /auth/login`
- `/api/*` (proxied to backend)

Examples:

- `POST http://localhost:8081/api/jobseekers/register`
- `GET http://localhost:8081/api/jobs`
- `PUT http://localhost:8081/api/jobseekers/{userId}/profile`
- `GET http://localhost:8081/api/jobseekers/{userId}/match`

## Useful URLs

- Backend Swagger UI: `http://localhost:8080/swagger-ui.html`
- Public GitHub repo: `https://github.com/adarshmalayath/GP7_CO7214GW2_submission`

## Notes

- Start order matters: backend -> gateway -> frontend.
- Login is handled at gateway level (`/auth/login`) using backend user data.
- If ports are in use, update:
  - `employment-service/src/main/resources/application.properties`
  - `api-gateway/.env`
  - `frontend/package.json`

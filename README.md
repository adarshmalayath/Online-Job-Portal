# Employment Service Stack (Backend + API Gateway + Frontend)

This repository includes:

- `employment-service` (Java Spring Boot backend)
- `api-gateway` (Node.js API gateway)
- `frontend` (React + Vite frontend with pages)

## 1. Run Backend (Spring Boot)

```bash
cd employment-service
mvn spring-boot:run
```

Backend URL: `http://localhost:8080`

## 2. Run API Gateway

Open a new terminal:

```bash
cd api-gateway
cp .env.example .env
npm install
npm run dev
```

Gateway URL: `http://localhost:8081`

Gateway endpoints:

- `POST /auth/login` (login for frontend)
- `GET /health`
- `/api/*` proxied to backend

## 3. Run Frontend

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

Frontend URL: `http://localhost:5173`

## Frontend Pages

- `/register` - Register a job seeker
- `/login` - Login with email + password
- `/dashboard` - Search jobs + view job matches
- `/profile` - Create/update profile (skills, preferences, location)

## Architecture

```text
Frontend (React, 5173)
       |
       v
API Gateway (Express, 8081)
       |
       v
Employment Service (Spring Boot, 8080)
```

## Notes

- Start backend first, then gateway, then frontend.
- Login uses gateway endpoint `POST /auth/login`, which validates against existing job seekers from backend.
- If ports conflict, change them in:
  - Backend: `employment-service/src/main/resources/application.properties`
  - Gateway: `api-gateway/.env`
  - Frontend: `frontend/package.json`

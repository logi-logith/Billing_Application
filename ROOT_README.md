# Subscription Billing & Payment Management System

A full-stack subscription billing platform — Spring Boot backend + React frontend — built as a personal learning project.

## Structure

This is a monorepo with two independently runnable components:

```
├── backend/     Spring Boot API (Java, MySQL, JWT auth) — see backend/README.md
└── frontend/    React UI (Vite) — see frontend/README.md
```

## Quick start

**Backend** (from `backend/`):
```bash
mvn spring-boot:run
```
Runs on `http://localhost:8080`. API docs at `http://localhost:8080/swagger-ui.html`.

**Frontend** (from `frontend/`):
```bash
npm install
npm run dev
```
Runs on `http://localhost:3000` and proxies API calls to the backend.

## Tech stack

| Layer | Tech |
|---|---|
| Backend | Java 17+, Spring Boot, Spring Security (JWT), Spring Cloud, Flyway, MySQL |
| Frontend | React, React Router, Axios, Vite |
| Docs | springdoc-openapi / Swagger UI |
| Deployment (planned) | Docker, Kubernetes / AWS (ECS or EKS), RDS, S3 + CloudFront |

See each component's own README for full setup details.

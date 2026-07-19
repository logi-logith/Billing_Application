# Subscription Billing & Payment Management System

A backend system for managing subscription billing, invoicing, payments, and automated billing cycles — built as a personal learning project to practice production-style Spring Boot architecture, security, and cloud-native deployment.

## Features

- **User Management** – registration, authentication, and role-based access (`ROLE_USER`, `ROLE_ADMIN`)
- **Invoice Management** – generate, view, and track invoices per subscription
- **Payment Processing** – record and manage payments against invoices
- **Billing Scheduler** – automated recurring billing cycles
- **Secure Admin Registration** – admin accounts created via a protected secret-key flow
- **JWT-based Authentication** – stateless auth secured with Spring Security 6.x
- **API Documentation** – interactive Swagger UI for exploring and testing endpoints

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17+ |
| Framework | Spring Boot, Spring Cloud |
| Security | Spring Security 6.x, JWT |
| Database | MySQL |
| Migrations | Flyway |
| API Docs | springdoc-openapi (Swagger UI) |
| Containerization | Docker |
| Orchestration | Kubernetes |

## Project Structure

```
io.github.logith.billing_application
├── config          # Security, OpenAPI, and other app-wide configuration
├── controller       # REST API endpoints
├── service
│   └── impl         # Service interfaces + implementations
├── repository        # Spring Data JPA repositories
├── entity            # JPA entities
├── dto                # Request/response objects
└── security           # JWT filters, providers, and auth logic
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- MySQL running locally or via Docker
- Docker (optional, for containerized run)

### 1. Clone the repository

```bash
git clone <your-repo-url>
cd billing-application
```

### 2. Configure the database

Create a MySQL database named `billing_db`, then update `src/main/resources/application.yml` (or `.properties`) with your DB credentials:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/billing_db
    username: your_username
    password: your_password
```

Flyway will automatically run migrations on startup to set up the schema.

### 3. Run the application

```bash
mvn spring-boot:run
```

The app will start on `http://localhost:8080` by default.

### 4. (Optional) Run with Docker

```bash
docker build -t billing-application .
docker run -p 8080:8080 billing-application
```

## API Documentation

Once the app is running, explore and test the API using Swagger UI:

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **Raw OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

Most endpoints require a JWT bearer token. Use the **Authorize** button in Swagger UI after logging in to attach your token to subsequent requests.

## Authentication Flow

1. **Register** a user via `/api/auth/register`
2. **Login** via `/api/auth/login` to receive a JWT
3. Include the token in the `Authorization` header as `Bearer <token>` for all protected endpoints
4. **Admin registration** requires an additional secret key, restricted to trusted setup flows

## Roles & Access

| Role | Access |
|---|---|
| `ROLE_USER` | Manage own subscriptions, invoices, and payments |
| `ROLE_ADMIN` | Full access, including user management and billing scheduler controls |

## Roadmap / Ideas

- Add email notifications for upcoming/overdue invoices
- Support multiple payment gateways
- Add usage-based billing tiers
- Deploy to a managed Kubernetes cluster with CI/CD via Azure Pipelines

## License

This is a personal learning project — feel free to fork and adapt it for your own practice.

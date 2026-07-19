# Subscription Billing & Payment Management System — Application Guide

This document explains **what the application does, how it works internally, and what it's capable of** — a functional walkthrough rather than a setup guide.

---

## 1. What This Application Is

A backend system that simulates a real-world **subscription billing platform** — similar to what companies like Stripe Billing or Chargebee provide internally. It manages the full lifecycle of a customer subscription: signing up, getting billed on a recurring schedule, generating invoices, recording payments, and tracking who owes what.

It's built to mirror how an enterprise billing system is actually structured, using an interface + `serviceImpl` layering pattern, role-based security, and cloud-native deployment (Docker/Kubernetes).

---

## 2. Core Capabilities

### 2.1 User Management
- Users can **register** and **log in**, receiving a JWT on successful authentication.
- Two roles exist:
  - `ROLE_USER` — manages their own subscriptions, invoices, and payments
  - `ROLE_ADMIN` — full visibility and control across all users, plus billing scheduler management
- Admin accounts can only be created through a **secret-key-protected registration flow** — this prevents anyone from self-promoting to admin, a common real-world security requirement.
- Roles are checked using `hasAuthority()` rather than `hasRole()`, giving explicit control over the `ROLE_` prefix and authority strings.

### 2.2 Invoice Management
- The system can **generate invoices** tied to a user's active subscription.
- Each invoice tracks amount due, status (e.g. pending, paid, overdue), and the billing period it covers.
- Users can view their own invoice history; admins can view invoices across all users.

### 2.3 Payment Processing
- Payments can be **recorded against an invoice**, updating its status once fully paid.
- The system is structured to support partial payments and payment history tracking per invoice.
- This module ties directly into the Invoice module — a payment doesn't exist independently of an invoice it's settling.

### 2.4 Billing Scheduler
- Automates the recurring part of "subscription billing" — periodically generating new invoices for active subscriptions without manual intervention.
- This is the piece that makes it a true *subscription* system rather than a one-off invoice generator — it runs on a schedule (e.g. monthly) and creates the next billing cycle's invoice automatically.
- Typically implemented with Spring's scheduling support, decoupled from the request/response cycle so it runs independently in the background.

### 2.5 Security Layer
- All protected endpoints require a **JWT bearer token**, verified on every request via a security filter chain.
- `SecurityContext` is populated per-request so downstream code (controllers, services) can access the authenticated user's identity and roles.
- Passwords and sensitive registration flows (like admin creation) are gated separately from normal user actions.

---

## 3. How a Typical Flow Works End-to-End

1. **A user registers** → account created with `ROLE_USER`.
2. **They log in** → receive a JWT.
3. **They subscribe** (conceptually — tied to your subscription/plan model).
4. **The Billing Scheduler** runs on its cycle and **generates an invoice** for the active subscription period.
5. **The user views their invoice** via the Invoice endpoints (authenticated with their JWT).
6. **The user (or an admin) records a payment** against that invoice.
7. **The invoice status updates** to reflect payment received.
8. This repeats every billing cycle, fully automated after the initial subscription setup.

---

## 4. Architectural Patterns Used (and Why They Matter)

| Pattern / Decision | What It Gives You |
|---|---|
| Interface + `serviceImpl` subpackage | Decouples what a service *does* from how it's implemented — easier to swap implementations or mock in tests |
| `FetchType.LAZY` on all relationships | Prevents unnecessary data loading (e.g. loading a user's full invoice history just to check their name) — better performance at scale |
| `hasAuthority()` over `hasRole()` | Finer-grained, explicit control over security expressions instead of relying on Spring's `ROLE_` prefix convention |
| Flyway migrations | Every schema change is versioned and repeatable — critical for a real deployment pipeline, not just local dev |
| Secret-key-gated admin registration | Prevents privilege escalation — a real vulnerability class in systems that expose open registration |

---

## 5. Deployment Capabilities

- **Dockerized** — the app can be built into a container image and run anywhere Docker runs.
- **Kubernetes-ready** — structured to be deployed as a scalable service, meaning multiple instances could run behind a load balancer as usage grows.
- **API-documented** — via springdoc-openapi, so anyone (including future-you) can explore and test every endpoint without reading the source code.

---

## 6. What This Project Demonstrates (Skill-Wise)

If you're using this as a portfolio or interview reference point, this project shows you can:
- Design and secure a multi-role REST API with JWT
- Model a real billing domain (subscriptions → invoices → payments) with correct relationships
- Automate recurring backend processes (the scheduler)
- Apply production-grade patterns (layered architecture, migrations, lazy loading)
- Document and containerize a service for deployment

---

## 7. Possible Next Capabilities (Not Yet Built)

These aren't implemented yet, but would be natural extensions given the current architecture:
- Email/notification alerts for upcoming or overdue invoices
- Multiple payment gateway integrations (Razorpay, Stripe, etc.)
- Usage-based or tiered billing (beyond flat recurring amounts)
- Refund handling
- Reporting/analytics endpoints for admins (revenue over time, churn, etc.)

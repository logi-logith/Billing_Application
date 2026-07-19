# Billing Frontend

React (Vite) frontend for the Subscription Billing & Payment Management System backend.

## Stack
- React 18 + React Router
- Axios for API calls
- Vite (dev server + build)
- Plain CSS with a small design-token system (`src/styles/tokens.css`) — no UI framework, so it's easy to see exactly what's happening

## Setup

```bash
npm install
npm run dev
```

Runs on `http://localhost:3000`. The dev server proxies any `/api/*` request to your Spring Boot backend on `http://localhost:8080` (see `vite.config.js`), so **you don't need to configure CORS just to develop locally**. You'll still want CORS configured on the backend for the eventual production deployment (S3/CloudFront calling a separately-hosted API).

## ⚠️ Before this will actually work

This scaffold guesses at your backend's exact request/response shapes. Check and adjust these to match your real controllers:

| File | What to check |
|---|---|
| `src/api/authService.js` | Exact paths for login/register/register-admin, and request body field names |
| `src/context/AuthContext.jsx` | The field name for the JWT in the login response (`token` vs `accessToken`), and the claim name your JWT uses for roles |
| `src/api/invoiceService.js`, `paymentService.js` | Exact endpoint paths and whether an admin "all invoices" endpoint exists yet |
| Dashboard/Invoices/Payments/Admin pages | Field names on your Invoice/Payment DTOs (`amount`, `status`, `billingPeriod`, etc.) |

The fastest way to line these up: open your Swagger UI (`/swagger-ui.html`) side-by-side with these files.

## Structure

```
src/
├── api/            # axios client + one service file per backend module
├── context/          # AuthContext — holds the JWT and derived user/role state
├── components/        # Layout (sidebar shell), route guards
├── pages/               # Login, Register, Dashboard, Invoices, Payments, Admin
└── styles/                # tokens.css (design system) + global.css
```

## Design notes

The visual direction is a "ledger" concept — parchment background, ink-navy text, a teal accent for settled/active states, amber for pending, rust for overdue. Invoice and payment rows use a dashed-divider "tear-off stub" styling with monospace figures, which is meant to feel like an actual financial ledger rather than a generic dashboard template.

## Production build

```bash
npm run build
```

Outputs static files to `dist/` — deployable directly to S3 + CloudFront, Netlify, Vercel, or any static host. Set `VITE_API_BASE_URL` (in a `.env.production` file) to your deployed backend's URL before building, since the dev proxy won't exist in production:

```
VITE_API_BASE_URL=https://api.yourdomain.com/api
```

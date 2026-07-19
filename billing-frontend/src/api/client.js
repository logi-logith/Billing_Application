import axios from 'axios';

// Base URL: in dev, Vite proxies /api to your Spring Boot backend (see vite.config.js).
// In production, point this at your deployed backend URL via an env var.
const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
});

// Attach the JWT to every request, if we have one.
client.interceptors.request.use((config) => {
  const token = localStorage.getItem('token'); // fine for a learning project;
  // for production, prefer an httpOnly cookie set by the backend instead.
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// If the token is expired/invalid, bounce back to login.
client.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default client;

import client from './client';

// NOTE: adjust these paths to match your actual @RequestMapping values.
export const authService = {
  login: (username, password) =>
    client.post('/auth/login', { username, password }),

  register: (username, email, password) =>
    client.post('/auth/register', { username, email, password }),

  // Admin registration requires the secret key your backend expects.
  registerAdmin: (username, email, password, secretKey) =>
    client.post('/auth/register-admin', { username, email, password, secretKey }),
};

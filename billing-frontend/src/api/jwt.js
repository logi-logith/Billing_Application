// Minimal JWT payload decoder — just enough to read the username/role
// claims your backend puts in the token. No signature verification here;
// the backend is the source of truth for whether the token is valid.
export function decodeToken(token) {
  try {
    const payload = token.split('.')[1];
    const decoded = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
    return JSON.parse(decoded);
  } catch {
    return null;
  }
}

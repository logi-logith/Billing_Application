import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    try {
      await login(username, password);
      navigate('/');
    } catch (err) {
      setError('Couldn\'t sign in — check your username and password.');
    }
  }

  return (
    <div className="auth-shell">
      <div className="auth-card">
        <h2>Sign in</h2>
        <p style={{ color: 'var(--color-ink-soft)', fontSize: 'var(--text-sm)', marginTop: -8, marginBottom: 24 }}>
          Access your subscriptions, invoices, and payments.
        </p>
        {error && <div className="error-text">{error}</div>}
        <form onSubmit={handleSubmit}>
          <div className="field">
            <label htmlFor="username">Username</label>
            <input id="username" value={username} onChange={(e) => setUsername(e.target.value)} required />
          </div>
          <div className="field">
            <label htmlFor="password">Password</label>
            <input id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
          </div>
          <button className="btn" type="submit">Sign in</button>
        </form>
        <p style={{ fontSize: 'var(--text-sm)', marginTop: 20, textAlign: 'center' }}>
          No account? <Link to="/register" style={{ color: 'var(--color-teal)' }}>Register</Link>
        </p>
      </div>
    </div>
  );
}

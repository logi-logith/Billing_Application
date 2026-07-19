import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authService } from '../api/authService';

export default function Register() {
  const [form, setForm] = useState({ username: '', email: '', password: '' });
  const [error, setError] = useState('');
  const [done, setDone] = useState(false);
  const navigate = useNavigate();

  function update(field) {
    return (e) => setForm({ ...form, [field]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    try {
      await authService.register(form.username, form.email, form.password);
      setDone(true);
      setTimeout(() => navigate('/login'), 1200);
    } catch (err) {
      setError('Registration failed — that username or email may already be taken.');
    }
  }

  return (
    <div className="auth-shell">
      <div className="auth-card">
        <h2>Create your account</h2>
        {done ? (
          <p>Account created. Redirecting to sign in…</p>
        ) : (
          <>
            {error && <div className="error-text">{error}</div>}
            <form onSubmit={handleSubmit}>
              <div className="field">
                <label htmlFor="username">Username</label>
                <input id="username" value={form.username} onChange={update('username')} required />
              </div>
              <div className="field">
                <label htmlFor="email">Email</label>
                <input id="email" type="email" value={form.email} onChange={update('email')} required />
              </div>
              <div className="field">
                <label htmlFor="password">Password</label>
                <input id="password" type="password" value={form.password} onChange={update('password')} required />
              </div>
              <button className="btn" type="submit">Create account</button>
            </form>
          </>
        )}
        <p style={{ fontSize: 'var(--text-sm)', marginTop: 20, textAlign: 'center' }}>
          Already registered? <Link to="/login" style={{ color: 'var(--color-teal)' }}>Sign in</Link>
        </p>
      </div>
    </div>
  );
}

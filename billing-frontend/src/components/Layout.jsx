import { NavLink, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Layout() {
  const { user, isAdmin, logout } = useAuth();

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="sidebar-brand">
          Ledger<span className="dot">.</span>
        </div>
        <nav>
          <NavLink to="/" end className={({ isActive }) => (isActive ? 'active' : '')}>
            Dashboard
          </NavLink>
          <NavLink to="/invoices" className={({ isActive }) => (isActive ? 'active' : '')}>
            Invoices
          </NavLink>
          <NavLink to="/payments" className={({ isActive }) => (isActive ? 'active' : '')}>
            Payments
          </NavLink>
          {isAdmin && (
            <NavLink to="/admin" className={({ isActive }) => (isActive ? 'active' : '')}>
              Admin
            </NavLink>
          )}
        </nav>
        <div className="sidebar-footer">
          <div style={{ marginBottom: 8 }}>{user?.username}</div>
          <button
            className="btn-secondary btn"
            style={{ width: 'auto', padding: '6px 12px' }}
            onClick={logout}
          >
            Log out
          </button>
        </div>
      </aside>
      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
}

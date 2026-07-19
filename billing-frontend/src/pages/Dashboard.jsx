import { useEffect, useState } from 'react';
import { invoiceService } from '../api/invoiceService';
import { useAuth } from '../context/AuthContext';

export default function Dashboard() {
  const { user } = useAuth();
  const [invoices, setInvoices] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    invoiceService
      .getMyInvoices()
      .then((res) => setInvoices(res.data))
      .catch(() => setInvoices([]))
      .finally(() => setLoading(false));
  }, []);

  const outstanding = invoices.filter((i) => i.status !== 'PAID');
  const totalDue = outstanding.reduce((sum, i) => sum + (i.amount || 0), 0);

  return (
    <>
      <div className="page-header">
        <div>
          <h1>Welcome back{user?.username ? `, ${user.username}` : ''}</h1>
          <p>Here's where your subscription billing stands today.</p>
        </div>
      </div>

      <div className="stat-grid">
        <div className="card stat-card">
          <div className="label">Open invoices</div>
          <div className="value">{loading ? '—' : outstanding.length}</div>
        </div>
        <div className="card stat-card">
          <div className="label">Total invoices</div>
          <div className="value">{loading ? '—' : invoices.length}</div>
        </div>
        <div className="card stat-card">
          <div className="label">Amount due</div>
          <div className="value">{loading ? '—' : `₹${totalDue.toFixed(2)}`}</div>
        </div>
      </div>

      <div className="card">
        <h3>Recent invoices</h3>
        {loading && <p>Loading…</p>}
        {!loading && invoices.length === 0 && <p>No invoices yet.</p>}
        {invoices.slice(0, 5).map((inv) => (
          <div className="ledger-row" key={inv.id}>
            <div className="meta">
              <span>Invoice #{inv.id}</span>
              <span className="id">{inv.billingPeriod || inv.createdAt}</span>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
              <span className={`badge ${(inv.status || '').toLowerCase()}`}>{inv.status}</span>
              <span className="amount">₹{inv.amount?.toFixed(2)}</span>
            </div>
          </div>
        ))}
      </div>
    </>
  );
}

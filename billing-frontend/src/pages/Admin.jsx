import { useEffect, useState } from 'react';
import { invoiceService } from '../api/invoiceService';

export default function Admin() {
  const [invoices, setInvoices] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    invoiceService
      .getAllInvoices()
      .then((res) => setInvoices(res.data))
      .catch(() => setInvoices([]))
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <div className="page-header">
        <div>
          <h1>Admin</h1>
          <p>Cross-user visibility into invoices and billing activity.</p>
        </div>
      </div>

      <div className="card">
        <h3>All invoices</h3>
        {loading && <p>Loading…</p>}
        {!loading && invoices.length === 0 && <p>No invoices found across any user yet.</p>}
        {invoices.map((inv) => (
          <div className="ledger-row" key={inv.id}>
            <div className="meta">
              <span>Invoice #{inv.id} — {inv.username || `user ${inv.userId}`}</span>
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

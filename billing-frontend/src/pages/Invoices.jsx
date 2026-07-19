import { useEffect, useState } from 'react';
import { invoiceService } from '../api/invoiceService';

export default function Invoices() {
  const [invoices, setInvoices] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    invoiceService
      .getMyInvoices()
      .then((res) => setInvoices(res.data))
      .catch(() => setInvoices([]))
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <div className="page-header">
        <div>
          <h1>Invoices</h1>
          <p>Every invoice generated for your subscription, oldest to newest.</p>
        </div>
      </div>

      <div className="card">
        {loading && <p>Loading…</p>}
        {!loading && invoices.length === 0 && <p>No invoices yet — they'll appear here once your first billing cycle runs.</p>}
        {invoices.map((inv) => (
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

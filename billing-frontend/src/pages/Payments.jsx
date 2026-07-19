import { useEffect, useState } from 'react';
import { paymentService } from '../api/paymentService';

export default function Payments() {
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({ invoiceId: '', amount: '' });
  const [error, setError] = useState('');

  function load() {
    setLoading(true);
    paymentService
      .getMyPayments()
      .then((res) => setPayments(res.data))
      .catch(() => setPayments([]))
      .finally(() => setLoading(false));
  }

  useEffect(load, []);

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    try {
      await paymentService.recordPayment(form.invoiceId, parseFloat(form.amount));
      setForm({ invoiceId: '', amount: '' });
      load();
    } catch {
      setError('Could not record that payment — check the invoice ID and try again.');
    }
  }

  return (
    <>
      <div className="page-header">
        <div>
          <h1>Payments</h1>
          <p>Record a payment against an invoice, and see your payment history.</p>
        </div>
      </div>

      <div className="card" style={{ marginBottom: 24 }}>
        <h3>Record a payment</h3>
        {error && <div className="error-text">{error}</div>}
        <form onSubmit={handleSubmit} style={{ display: 'flex', gap: 16, alignItems: 'flex-end' }}>
          <div className="field" style={{ marginBottom: 0, flex: 1 }}>
            <label htmlFor="invoiceId">Invoice ID</label>
            <input
              id="invoiceId"
              value={form.invoiceId}
              onChange={(e) => setForm({ ...form, invoiceId: e.target.value })}
              required
            />
          </div>
          <div className="field" style={{ marginBottom: 0, flex: 1 }}>
            <label htmlFor="amount">Amount</label>
            <input
              id="amount"
              type="number"
              step="0.01"
              value={form.amount}
              onChange={(e) => setForm({ ...form, amount: e.target.value })}
              required
            />
          </div>
          <button className="btn" type="submit" style={{ width: 160 }}>Record</button>
        </form>
      </div>

      <div className="card">
        <h3>Payment history</h3>
        {loading && <p>Loading…</p>}
        {!loading && payments.length === 0 && <p>No payments recorded yet.</p>}
        {payments.map((p) => (
          <div className="ledger-row" key={p.id}>
            <div className="meta">
              <span>Payment #{p.id}</span>
              <span className="id">Against invoice #{p.invoiceId}</span>
            </div>
            <span className="amount">₹{p.amount?.toFixed(2)}</span>
          </div>
        ))}
      </div>
    </>
  );
}

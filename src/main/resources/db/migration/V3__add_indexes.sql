CREATE INDEX idx_user_email           ON users(email);

--Subscription
CREATE INDEX idx_sub_user_id          ON subscriptions(user_id);
CREATE INDEX idx_sub_status           ON subscriptions(status);
CREATE INDEX idx_sub_next_billing     ON subscriptions(next_billing_date);
CREATE INDEX idx_sub_status_billing   ON subscriptions(status, next_billing_date);

--Invoice
CREATE INDEX idx_inv_subscription_id  ON invoices(subscription_id);
CREATE INDEX idx_inv_status           ON invoices(status);
CREATE INDEX idx_inv_due_date         ON invoices(due_date);

--Payments
CREATE INDEX idx_pay_invoice_id       ON payments(invoice_id);
CREATE INDEX idx_pay_status           ON payments(status);
CREATE INDEX idx_pay_transaction      ON payments(transaction_id);
ALTER TABLE subscriptions
    ADD CONSTRAINT fk_sub_user
        FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE subscriptions
    ADD CONSTRAINT fk_sub_plan
        FOREIGN KEY (plan_id) REFERENCES plans(id);

ALTER TABLE invoices
    ADD CONSTRAINT fk_inv_subscription
        FOREIGN KEY (subscription_id) REFERENCES subscriptions(id);

ALTER TABLE payments
    ADD CONSTRAINT fk_pay_invoice
        FOREIGN KEY (invoice_id) REFERENCES invoices(id);
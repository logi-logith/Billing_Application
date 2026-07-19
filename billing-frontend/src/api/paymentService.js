import client from './client';

export const paymentService = {
  getMyPayments: () => client.get('/payments'),
  recordPayment: (invoiceId, amount) =>
    client.post('/payments', { invoiceId, amount }),
};

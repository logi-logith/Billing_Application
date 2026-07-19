import client from './client';

export const invoiceService = {
  getMyInvoices: () => client.get('/invoices'),
  getInvoiceById: (id) => client.get(`/invoices/${id}`),
  getAllInvoices: () => client.get('/invoices/all'), // admin-only on the backend
};

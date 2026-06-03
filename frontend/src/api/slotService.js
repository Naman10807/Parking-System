import axiosClient from './axiosClient';

export const slotService = {
  getAll: () => axiosClient.get('/slots'),
  getById: (id) => axiosClient.get(`/slots/${id}`),
  create: (payload) => axiosClient.post('/slots', payload),
  update: (id, payload) => axiosClient.put(`/slots/${id}`, payload),
  remove: (id) => axiosClient.delete(`/slots/${id}`),
};

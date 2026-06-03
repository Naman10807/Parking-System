import axiosClient from './axiosClient';

export const parkingService = {
  registerEntry: (payload) => axiosClient.post('/parking/entry', payload),
  processExit: (vehicleNumber) => axiosClient.post(`/parking/exit/${vehicleNumber}`),
  getHistory: (search = '') =>
    axiosClient.get('/parking/history', { params: { search: search || undefined } }),
};

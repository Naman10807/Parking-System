import axiosClient from './axiosClient';

export const historyService = {
  getAll: () => axiosClient.get('/history'),
  getByVehicle: (vehicleNumber) => axiosClient.get(`/history/vehicle/${vehicleNumber}`),
  getByDateRange: (startDate, endDate) =>
    axiosClient.get('/history/date-range', { params: { startDate, endDate } }),
};

import axiosClient from './axiosClient';

export const authService = {
  login: (credentials) => axiosClient.post('/auth/login', credentials),
  register: (payload) => axiosClient.post('/auth/register', payload),
};

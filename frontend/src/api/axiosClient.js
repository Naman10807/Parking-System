import axios from 'axios';
import { getErrorMessage, shouldNotifyGlobally } from '../utils/apiError';
import { notifyError } from '../utils/toastBridge';

const axiosClient = axios.create({
  // Default /api uses Vite dev proxy (vite.config.js) — same origin, no CORS.
  // Set VITE_API_BASE_URL=http://localhost:8081/api only if not using the proxy.
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 15000,
});

axiosClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('sp_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

axiosClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;

    if (status === 401) {
      const onLoginPage = window.location.pathname.includes('/login');
      if (!onLoginPage) {
        notifyError(getErrorMessage(error));
        localStorage.removeItem('sp_token');
        localStorage.removeItem('sp_username');
        localStorage.removeItem('sp_role');
        window.location.href = '/login';
      }
      return Promise.reject(error);
    }

    if (shouldNotifyGlobally(error)) {
      notifyError(getErrorMessage(error));
    }

    return Promise.reject(error);
  }
);

export default axiosClient;

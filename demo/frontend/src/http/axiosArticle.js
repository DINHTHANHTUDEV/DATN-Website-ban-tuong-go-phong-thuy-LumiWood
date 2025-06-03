import axios from 'axios';
import { useAuthStore } from '@/store/auth';
import { getCartSessionId } from '@/utils/cartSession';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  }
});

apiClient.interceptors.request.use(
  config => {
    let authStore;
    try {
      authStore = useAuthStore();
    } catch (error) {
      return config;
    }

    if (authStore.isAuthenticated && authStore.token) {
      config.headers['Authorization'] = `Bearer ${authStore.token}`;
      delete config.headers['X-Cart-Session-Id'];
    }
    else {
      const sessionId = getCartSessionId();
      if (sessionId) {
        config.headers['X-Cart-Session-Id'] = sessionId;
      }
      delete config.headers['Authorization'];
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  response => response,
  error => {
    if (error.response) {
      const originalRequest = error.config;
      const status = error.response.status;

      if (status === 401 && !originalRequest._retry) {
        let authStore;
        try {
          authStore = useAuthStore();
        } catch (storeError) {
          return Promise.reject(error);
        }

        if (authStore.isAuthenticated) {
          authStore.logout();
          if (typeof router !== 'undefined' && router) {
            router.push({ name: 'login', query: { sessionExpired: 'true' } }).catch(()=>{});
          }
        }
      }
    }
    return Promise.reject(error);
  }
);

export default apiClient;

import axios from 'axios';
import { useUserStore } from '@/store/user';
import router from '@/router';

const service = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

service.interceptors.request.use((config) => {
  const userStore = useUserStore();
  if (userStore.token) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${userStore.token}`;
  }
  return config;
});

service.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const response = error.response;
    if (response && response.status === 401) {
      const userStore = useUserStore();
      userStore.logout();
      router.push({ name: 'Login' });
    }
    return Promise.reject(error);
  }
);

export default service;

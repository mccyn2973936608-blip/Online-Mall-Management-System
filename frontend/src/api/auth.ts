import request from './request';

export function login(payload: { username: string; password: string; rememberMe: boolean }) {
  return request.post('/auth/login', payload);
}

export function register(payload: { username: string; password: string; phone: string; email: string }) {
  return request.post('/auth/register', payload);
}

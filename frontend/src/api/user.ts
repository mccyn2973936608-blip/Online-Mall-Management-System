import request from './request';

export function getProfile() {
  return request.get('/user/profile');
}

export function updateProfile(payload: any) {
  return request.put('/user/profile', payload);
}

export function updatePassword(payload: any) {
  return request.put('/user/password', payload);
}

export function getMyOrders() {
  return request.get('/user/orders');
}

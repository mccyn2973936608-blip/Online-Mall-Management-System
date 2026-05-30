import request from './request';

export function addCart(payload: any) {
  return request.post('/cart/add', payload);
}

export function fetchCart() {
  return request.get('/cart/list');
}

export function updateCart(payload: any) {
  return request.put('/cart/update', payload);
}

export function deleteCart(id: number) {
  return request.delete(`/cart/delete/${id}`);
}

import request from './request';

export function submitOrder(payload: any) {
  return request.post('/order/submit', payload);
}

export function fetchMyOrders(params: any) {
  return request.get('/order/list', { params });
}

export function cancelOrder(id: number) {
  return request.put(`/order/cancel/${id}`);
}

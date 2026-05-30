import request from './request';

export function fetchAdminUsers(params: any) {
  return request.get('/admin/user/list', { params });
}

export function createAdminUser(payload: any) {
  return request.post('/admin/user/create', payload);
}

export function updateAdminUser(payload: any) {
  return request.put('/admin/user/update', payload);
}

export function deleteAdminUser(id: number) {
  return request.delete(`/admin/user/delete/${id}`);
}

export function fetchCategories() {
  return request.get('/admin/category/list');
}

export function createCategory(payload: any) {
  return request.post('/admin/category/create', payload);
}

export function updateCategory(payload: any) {
  return request.put('/admin/category/update', payload);
}

export function deleteCategory(id: number) {
  return request.delete(`/admin/category/delete/${id}`);
}

export function fetchAdminOrders(params: any) {
  return request.get('/admin/order/list', { params });
}

export function confirmOrder(id: number) {
  return request.put(`/admin/order/confirm/${id}`);
}

export function completeOrder(id: number) {
  return request.put(`/admin/order/complete/${id}`);
}

export function cancelAdminOrder(id: number) {
  return request.put(`/admin/order/cancel/${id}`);
}

export function fetchOrderDetail(id: number) {
  return request.get(`/admin/order/${id}`);
}

import request from './request';

export function fetchProducts(params: any) {
  return request.get('/product/list', { params });
}

export function fetchProductDetail(id: number) {
  return request.get(`/product/${id}`);
}

export function fetchCategories() {
  return request.get('/product/categories');
}

export function createProduct(payload: any) {
  return request.post('/product/admin/create', payload);
}

export function updateProduct(payload: any) {
  return request.put('/product/admin/update', payload);
}

export function changeProductStatus(id: number, status: number) {
  return request.put(`/product/admin/status/${id}`, null, { params: { status } });
}

export function deleteProduct(id: number) {
  return request.delete(`/product/admin/delete/${id}`);
}

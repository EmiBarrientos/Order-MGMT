import { apiGet, apiPost, apiPut, apiDelete } from "./api/client";

export function getAllProducts() {
  return apiGet("/api/product/findall");
}

export function createProduct(product) {
  return apiPost("/api/product/save", product);
}

export function updateProduct( id, product) {
  return apiPut(`/api/product/update/${id}`, product);
}

export function deleteProduct(id) {
  return apiDelete(`/api/product/delete/${id}`);
}

import { apiGet, apiPost } from "./api/client";

export function getAllProducts() {
  return apiGet("/api/product/findall");
}

export function createProduct(product) {
  return apiPost("/api/product/save", product);
}

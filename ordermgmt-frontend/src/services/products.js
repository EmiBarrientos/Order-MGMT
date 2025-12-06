import { apiGet } from "./api/client";

export function getAllProducts() {
  return apiGet("/api/product/findall");
}
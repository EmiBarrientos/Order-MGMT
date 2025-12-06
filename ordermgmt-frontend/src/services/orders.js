import { apiGet } from "./api/client";

export function getAllOrders() {
  return apiGet("/api/order/with-products");
}
package com.ordermgmt.orders.service;

import com.ordermgmt.orders.dto.OrderDto;
import com.ordermgmt.orders.dto.OrderOutputDto;
import com.ordermgmt.orders.https.response.ProductByIdResponse;

import java.util.List;

public interface IOrderService {

   void saveOrder(OrderDto orderDto);

   void updateOrder(OrderDto orderDto, Long id);

   void deleteOrder(Long id);

   OrderDto getOrderByid( Long id);

   List<OrderDto> getAllOrders();

   ProductByIdResponse findProductById(Long productId);

   List<OrderOutputDto> getAllWhitProduct();
}

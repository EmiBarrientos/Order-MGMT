package com.ordermgmt.orders.service;

import com.ordermgmt.orders.dto.OrderDto;
import com.ordermgmt.orders.dto.OrderOutputDto;
import com.ordermgmt.orders.https.response.ProductByIdResponse;

import java.util.List;

public interface IOrderService {

   void saveOrder(OrderDto orderDto);

   void updateOrder(OrderDto orderDto, Integer id);

   void deleteOrder(Integer id);

   OrderDto getOrderByid(Integer id);

   List<OrderDto> getAllOrders();

   ProductByIdResponse findProductById(Integer productId);

   List<OrderOutputDto> getAllWhitProduct();
}

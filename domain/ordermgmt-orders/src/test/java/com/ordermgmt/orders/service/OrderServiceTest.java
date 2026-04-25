package com.ordermgmt.orders.service;

import com.ordermgmt.orders.client.ProductClient;
import com.ordermgmt.orders.dto.OrderDto;
import com.ordermgmt.orders.dto.OrderOutputDto;
import com.ordermgmt.orders.dto.ProductDto;
import com.ordermgmt.orders.entity.Order;
import com.ordermgmt.orders.enums.EstadoPedido;
import com.ordermgmt.orders.exceptions.custom.ExternalServiceException;
import com.ordermgmt.orders.exceptions.custom.OrderNotFoundException;
import com.ordermgmt.orders.repository.IOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldReturnOrderDto_whenOrderExists() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(10L);
        order.setEstadoPedido(EstadoPedido.valueOf("PENDIENTE"));
        order.setIdProducto(List.of(1L, 2L));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDto result = orderService.getOrderByid(1L);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(10, result.getUserId());
    }


    @Test
    void shouldThrowException_whenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderByid(1L);
        });
    }

    @Test
    void shouldSaveOrder() {
        OrderDto dto = new OrderDto();
        dto.setId(1L);
        dto.setUserId(10L);
        dto.setEstadoPedido(EstadoPedido.valueOf("PENDIENTE"));
        dto.setIdProducto(List.of(1L));

        orderService.saveOrder(dto);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void shouldReturnProduct_whenCallingProductClient() {
        ProductDto product = new ProductDto();
        product.setProductName("Test");
        product.setPrice(100.0);
        product.setStock(5.0);

        when(productClient.findProductById(1L)).thenReturn(product);

        var response = orderService.findProductById(1L);

        assertEquals("Test", response.getProductName());
    }
    @Test
    void shouldThrowExternalServiceException_whenProductClientFails() {
        when(productClient.findProductById(1L))
                .thenThrow(new RuntimeException("Error"));

        assertThrows(ExternalServiceException.class, () -> {
            orderService.findProductById(1L);
        });
    }
    @Test
    void shouldUpdateOrder_whenOrderExists() {
        Order order = new Order();
        order.setId(1L);

        OrderDto dto = new OrderDto();
        dto.setId(1L);
        dto.setUserId(20L);
        dto.setEstadoPedido(EstadoPedido.valueOf("LISTO"));
        dto.setIdProducto(List.of(2L));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.updateOrder(dto, 1L);

        assertEquals(EstadoPedido.valueOf("LISTO"), order.getEstadoPedido());
        assertEquals(20L, order.getUserId());
        verify(orderRepository).save(order);
    }
    @Test
    void shouldThrowException_whenUpdatingNonExistingOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.updateOrder(new OrderDto(), 1L);
        });
    }
    @Test
    void shouldDeleteOrder_whenExists() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        orderService.deleteOrder(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    void shouldThrowException_whenDeletingNonExistingOrder() {
        when(orderRepository.existsById(1L)).thenReturn(false);

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.deleteOrder(1L);
        });
    }
    @Test
    void shouldReturnAllOrders() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(10L);
        order.setEstadoPedido(EstadoPedido.valueOf("PENDIENTE"));
        order.setIdProducto(List.of(1L));

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderDto> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void shouldReturnOrdersWithProducts() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(10L);
        order.setEstadoPedido(EstadoPedido.valueOf("PENDIENTE"));
        order.setIdProducto(List.of(1L, 2L));

        ProductDto p1 = new ProductDto();
        p1.setId(1L);
        p1.setProductName("Prod1");

        ProductDto p2 = new ProductDto();
        p2.setId(2L);
        p2.setProductName("Prod2");

        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(productClient.getProductsByIds(anyList()))
                .thenReturn(List.of(p1, p2));

        List<OrderOutputDto> result = orderService.getAllWhitProduct();

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getProductDtoList().size());
    }

    @Test
    void shouldReturnOrdersWithEmptyProducts_whenNoProductIds() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(10L);
        order.setEstadoPedido(EstadoPedido.valueOf("PENDIENTE"));
        order.setIdProducto(Collections.emptyList());

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderOutputDto> result = orderService.getAllWhitProduct();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getProductDtoList().isEmpty());
    }



}
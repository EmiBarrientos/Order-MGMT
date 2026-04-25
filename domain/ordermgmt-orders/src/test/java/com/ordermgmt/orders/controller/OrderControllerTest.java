package com.ordermgmt.orders.controller;

import com.ordermgmt.orders.dto.OrderDto;
import com.ordermgmt.orders.dto.OrderOutputDto;
import com.ordermgmt.orders.dto.ProductDto;
import com.ordermgmt.orders.enums.EstadoPedido;
import com.ordermgmt.orders.exceptions.custom.ExternalServiceException;

import com.ordermgmt.orders.exceptions.custom.OrderNotFoundException;
import com.ordermgmt.orders.exceptions.handler.GlobalExceptionHandler;
import com.ordermgmt.orders.https.response.ProductByIdResponse;
import com.ordermgmt.orders.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(OrderController.class)
@Import(GlobalExceptionHandler.class)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;


    @Test
    void shouldReturnOrder_whenExists() throws Exception {

        OrderDto dto = OrderDto.builder()
                .id(1L)
                .userId(10L)
                .estadoPedido(EstadoPedido.valueOf("PENDIENTE"))
                .idProducto(List.of(1L))
                .build();

        when(orderService.getOrderByid(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(10));
    }
    @Test
    void shouldReturn404_whenOrderNotFound() throws Exception {

        when(orderService.getOrderByid(1L))
                .thenThrow(new OrderNotFoundException(1L));

        mockMvc.perform(get("/api/order/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void shouldCreateOrder() throws Exception {

        String json = """
        {
            "id": 1,
            "userId": 10,
            "estadoPedido": "PENDIENTE",
            "idProducto": [1]
        }
    """;

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(orderService).saveOrder(any(OrderDto.class));
    }

    @Test
    void shouldDeleteOrder() throws Exception {

        mockMvc.perform(delete("/api/order/1"))
                .andExpect(status().isOk());

        verify(orderService).deleteOrder(1L);
    }
    @Test
    void shouldReturn404_whenDeletingNonExistingOrder() throws Exception {

        doThrow(new OrderNotFoundException(1L))
                .when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/api/order/1"))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldReturnAllOrders() throws Exception {

        OrderDto dto = OrderDto.builder()
                .id(1L)
                .userId(10L)
                .estadoPedido(EstadoPedido.valueOf("PENDIENTE"))
                .idProducto(List.of(1L))
                .build();

        when(orderService.getAllOrders()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(10));
    }

    @Test
    void shouldUpdateOrder() throws Exception {

        String json = """
        {
            "id": 1,
            "userId": 20,
            "estadoPedido": "LISTO",
            "idProducto": [2]
        }
    """;

        mockMvc.perform(put("/api/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Order Updated Successfully"));

        verify(orderService).updateOrder(any(OrderDto.class), eq(1L));
    }

    @Test
    void shouldReturn404_whenUpdatingNonExistingOrder() throws Exception {

        doThrow(new OrderNotFoundException(1L))
                .when(orderService).updateOrder(any(OrderDto.class), eq(1L));

        String json = """
        {
            "id": 1,
            "userId": 20,
            "estadoPedido": "LISTO",
            "idProducto": [2]
        }
    """;

        mockMvc.perform(put("/api/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnProductById() throws Exception {

        ProductByIdResponse response = ProductByIdResponse.builder()
                .productName("Test Product")
                .Price(100.0)
                .stock(5.0)
                .build();

        when(orderService.findProductById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/order/find-product-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }

    @Test
    void shouldReturn502_whenProductServiceFails() throws Exception {

        when(orderService.findProductById(1L))
                .thenThrow(new ExternalServiceException("Error"));

        mockMvc.perform(get("/api/order/find-product-by-id/1"))
                .andExpect(status().isBadGateway());
    }

    @Test
    void shouldReturnOrdersWithProducts() throws Exception {

        // Arrange
        ProductDto product = new ProductDto();
        product.setId(1L);
        product.setProductName("Test Product");

        OrderOutputDto dto = new OrderOutputDto(
                1L,
                10L,
                List.of(product),
                EstadoPedido.valueOf("PENDIENTE")
        );

        when(orderService.getAllWhitProduct()).thenReturn(List.of(dto));

        // Act & Assert
        mockMvc.perform(get("/api/order/with-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(10))
                .andExpect(jsonPath("$[0].estadoPedido").value("PENDIENTE"))
                .andExpect(jsonPath("$[0].productDtoList[0].productName").value("Test Product"));
    }

    @Test
    void shouldReturnOrderById() throws Exception {

        OrderDto dto = OrderDto.builder()
                .id(1L)
                .userId(10L)
                .estadoPedido(EstadoPedido.valueOf("PENDIENTE"))
                .idProducto(List.of(1L))
                .build();

        when(orderService.getOrderByid(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
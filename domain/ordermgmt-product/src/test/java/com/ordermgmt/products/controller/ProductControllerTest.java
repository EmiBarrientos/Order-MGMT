package com.ordermgmt.products.controller;

import com.ordermgmt.products.dto.ProductDto;
import com.ordermgmt.products.exceptions.custom.ProductNotFoundException;
import com.ordermgmt.products.service.ProductService;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import com.fasterxml.jackson.databind.ObjectMapper;



import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
@Import(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllProducts() throws Exception {
        ProductDto dto = ProductDto.builder()
                .id(1L)
                .productName("Mouse")
                .price(BigDecimal.valueOf(100.0))
                .stock(10)
                .build();

        when(productService.getProducts()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
    @Test
    void shouldReturnProductById() throws Exception {
        // Arrange
        ProductDto dto = ProductDto.builder()
                .id(1L)
                .productName("Mouse")
                .price(BigDecimal.valueOf(100.0))
                .stock(10)
                .build();

        when(productService.getProductById(1L)).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Mouse"));
    }


    @Test
    void shouldSaveProduct() throws Exception {
        // Arrange
        ProductDto dto = ProductDto.builder()
                .id(1L)
                .productName("Mouse")
                .price(BigDecimal.valueOf(100.0))
                .stock(10)
                .build();

        // Act + Assert
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Mouse"));

        verify(productService).saveProduct(any(ProductDto.class));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product Deleted Successfully"));

        verify(productService).deleteProduct(1L);
    }
    @Test
    void shouldUpdateProduct() throws Exception {
        ProductDto dto = ProductDto.builder()
                .id(1L)
                .productName("Mouse")
                .price(BigDecimal.valueOf(150.0))
                .stock(20)
                .build();

        mockMvc.perform(put("/api/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product Updated Successfully"));

        verify(productService).updateProduct(eq(1L), any(ProductDto.class));
    }

    @Test
    void shouldReturnProductsByIds() throws Exception {
        List<Long> ids = List.of(1L, 2L);

        ProductDto dto = ProductDto.builder()
                .id(1L)
                .productName("Mouse")
                .price(BigDecimal.valueOf(100.0))
                .stock(10)
                .build();

        when(productService.findByIds(ids)).thenReturn(List.of(dto));

        mockMvc.perform(post("/api/product/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
    @Test
    void shouldReturnEmptyList_whenIdsEmpty() throws Exception {
        List<Long> ids = List.of();

        mockMvc.perform(post("/api/product/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void shouldReturnPayloadTooLarge_whenTooManyIds() throws Exception {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < 1001; i++) {
            ids.add((long) i);
        }

        mockMvc.perform(post("/api/product/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isPayloadTooLarge());
    }

    @Test
    void shouldReturn404_whenProductNotFound() throws Exception {

        when(productService.getProductById(1L))
                .thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

}
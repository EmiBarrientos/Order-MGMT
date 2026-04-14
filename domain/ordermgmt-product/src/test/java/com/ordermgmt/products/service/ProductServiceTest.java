package com.ordermgmt.products.service;

import com.ordermgmt.products.dto.ProductDto;
import com.ordermgmt.products.entity.Product;
import com.ordermgmt.products.repository.IProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private IProductRepository iProductRepository;

    @InjectMocks
    private ProductService productService;


    @Test
    void shouldSaveProduct_whenNameDoesNotExist(){
        ProductDto dto = new ProductDto();
        dto.setId(1L);
        dto.setProductName("Mouse");
        dto.setPrice(BigDecimal.valueOf(100.0));
        dto.setStock(10);

        when(iProductRepository.existsByProductName("Mouse")).thenReturn(false);
        productService.saveProduct(dto);
        verify(iProductRepository).save(any(Product.class));


    }

    @Test
    void shouldThrowException_whenProductNameAlreadyExists() {
        // Arrange
        ProductDto dto = new ProductDto();
        dto.setProductName("Mouse");

        when(iProductRepository.existsByProductName("Mouse")).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalStateException.class, () -> {
            productService.saveProduct(dto);
        });

        verify(iProductRepository, never()).save(any());
    }



    @Test
    void shouldDeleteProductById() {
        // Arrange
        Long id = 1L;
        // Act
        productService.deleteProduct(id);
        // Assert
        verify(iProductRepository).deleteById(id);
        if (!iProductRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
    }

    @Test
    void shouldUpdateProduct_whenProductExists() {
        // Arrange
        Long id = 1L;

        Product existingProduct = new Product();

        existingProduct.setProductName("Viejo");
        existingProduct.setPrice(BigDecimal.valueOf(50.0));
        existingProduct.setStock(5);

        ProductDto dto = new ProductDto();

        dto.setProductName("Nuevo");
        dto.setPrice(BigDecimal.valueOf(100.0));
        dto.setStock(10);

        when(iProductRepository.findById(id)).thenReturn(Optional.of(existingProduct));

        // Act
        productService.updateProduct(id, dto);
        // Assert
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(iProductRepository).save(captor.capture());

        Product updated = captor.getValue();

        assertEquals("Nuevo", updated.getProductName());
        assertEquals(100.0, updated.getPrice());
        assertEquals(10, updated.getStock());
    }


    @Test
    void shouldDoNothing_whenProductDoesNotExist() {
        // Arrange
        Long id = 1L;
        ProductDto dto = new ProductDto();

        when(iProductRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        productService.updateProduct(id, dto);

        // Assert
        verify(iProductRepository, never()).save(any());
    }


    @Test
    void shouldReturnProductDto_whenProductExists() {
        // Arrange
        Long id = 1L;

        Product product = new Product();
        product.setId(id);
        product.setProductName("Mouse");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setStock(10);

        when(iProductRepository.findById(id)).thenReturn(Optional.of(product));

        // Act
        ProductDto result = productService.getProductById(id);

        // Assert
        assertNotNull(result);
        assertEquals("Mouse", result.getProductName());
        assertEquals(100.0, result.getPrice());
        assertEquals(10, result.getStock());
        assertEquals(id, result.getId());
    }

    @Test
    void shouldThrowException_whenProductNotFound() {
        // Arrange
        Long id = 1L;

        when(iProductRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NullPointerException.class, () -> {
            productService.getProductById(id);
        });
    }

    @Test
    void shouldReturnListOfProductDtos_whenProductsExist() {
        // Arrange
        List<Long> ids = List.of(1L, 2L);

        Product p1 = new Product();
        p1.setId(1L);
        p1.setProductName("Mouse");
        p1.setPrice(BigDecimal.valueOf(100.0));
        p1.setStock(10);

        Product p2 = new Product();
        p2.setId(2L);
        p2.setProductName("Teclado");
        p2.setPrice(BigDecimal.valueOf(200.0));
        p2.setStock(5);

        when(iProductRepository.findByIdIn(ids)).thenReturn(List.of(p1, p2));

        // Act
        List<ProductDto> result = productService.findByIds(ids);

        // Assert
        assertEquals(2, result.size());

        assertEquals("Mouse", result.get(0).getProductName());
        assertEquals("Teclado", result.get(1).getProductName());
    }


    @Test
    void shouldReturnEmptyList_whenNoProductsFound() {
        // Arrange
        List<Long> ids = List.of(1L, 2L);

        when(iProductRepository.findByIdIn(ids)).thenReturn(Collections.emptyList());

        // Act
        List<ProductDto> result = productService.findByIds(ids);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllProducts() {
        // Arrange
        Product p1 = new Product();
        p1.setId(1L);
        p1.setProductName("Mouse");
        p1.setPrice(BigDecimal.valueOf(100.0));
        p1.setStock(10);

        Product p2 = new Product();
        p2.setId(2L);
        p2.setProductName("Teclado");
        p2.setPrice(BigDecimal.valueOf(200.0));
        p2.setStock(5);

        when(iProductRepository.findAll()).thenReturn(List.of(p1, p2));

        // Act
        List<ProductDto> result = productService.getProducts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Mouse", result.get(0).getProductName());
        assertEquals("Teclado", result.get(1).getProductName());
    }
    @Test
    void shouldReturnEmptyList_whenNoProductsExist() {
        // Arrange
        when(iProductRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ProductDto> result = productService.getProducts();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
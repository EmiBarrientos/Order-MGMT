package com.ordermgmt.products.service;

import com.ordermgmt.products.dto.ProductDto;

import java.util.List;

public interface IProductService {
    void saveProduct(ProductDto productDto);
    void deleteProduct(Integer id);
    void updateProduct(Integer id, ProductDto productDto);
    List<ProductDto> getProducts();
    ProductDto getProductById(Integer id);
}

package com.ordermgmt.products.service;

import com.ordermgmt.products.dto.ProductDto;
import com.ordermgmt.products.entity.Product;

import java.util.List;

public interface IProductService {
    void saveProduct(ProductDto productDto);
    void deleteProduct(Long id);
    void updateProduct(Long id, ProductDto productDto);
    List<ProductDto> getProducts();
    ProductDto getProductById(Long id);
    List<ProductDto> findByIds(List<Long> ids);

}

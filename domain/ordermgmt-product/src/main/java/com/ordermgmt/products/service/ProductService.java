package com.ordermgmt.products.service;

import com.ordermgmt.products.dto.ProductDto;
import com.ordermgmt.products.entity.Product;
import com.ordermgmt.products.exceptions.custom.ProductNotFoundException;
import com.ordermgmt.products.repository.IProductRepository;
import org.springframework.stereotype.Service;


import java.util.List;



@Service
public class ProductService implements IProductService{

    private final IProductRepository iProductRepository;


    public ProductService(IProductRepository IProductRepository) {
        this.iProductRepository = IProductRepository;
    }


    @Override
    public void saveProduct(ProductDto productDto) {

        if (iProductRepository.existsByProductName(productDto.getProductName())) {
            throw new IllegalStateException("Product name already exists");
        }

            Product product=new Product();
            product.setId(productDto.getId());
            product.setProductName(productDto.getProductName());
            product.setPrice(productDto.getPrice());
            product.setStock(productDto.getStock());
            iProductRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!iProductRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        iProductRepository.deleteById(id);

    }

    @Override
    public void updateProduct(Long id, ProductDto productDto) {
        Product product=iProductRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Product with id "+id+" not found"));
        product.setProductName(productDto.getProductName());
        product.setStock(productDto.getStock());
        product.setPrice(productDto.getPrice());
        iProductRepository.save(product);

    }

    @Override
    public List<ProductDto> getProducts() {
        List<ProductDto> productDtoList =iProductRepository.findAll()
                .stream()
                .map(product -> ProductDto.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .stock(product.getStock())
                        .price(product.getPrice())
                        .build()

                ).toList();
        return productDtoList;
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product=iProductRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Product with id "+id+" not found"));

        ProductDto productDto=ProductDto.builder()
                .productName(product.getProductName())
                .stock(product.getStock())
                .price(product.getPrice())
                .id(product.getId())
                .build();

      return productDto;
    }


    public List<ProductDto> findByIds(List<Long> ids) {
        List<Product> products = iProductRepository.findByIdIn(ids);

        return products.stream()
                .map(this::toDto)
                .toList();
    }

    private ProductDto toDto(Product product) {
        ProductDto productDto= ProductDto.builder()
                .price(product.getPrice())
                .productName(product.getProductName())
                .stock(product.getStock())
                .id(product.getId())
                .build();

        return productDto;
    }
}



/*

*
public List<ProductDto> findByIds(List<Long> ids) {
        List<Product> products = iProductRepository.findAllById(ids); // JPA: SELECT ... WHERE id IN (...)
        return products.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


**/
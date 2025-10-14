package com.ordermgmt.products.service;

import com.ordermgmt.products.dto.ProductDto;
import com.ordermgmt.products.entity.Product;
import com.ordermgmt.products.repository.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService implements IProductService{

    private final IProductRepository IProductRepository;

    public ProductService(IProductRepository IProductRepository) {
        this.IProductRepository = IProductRepository;
    }


    @Override
    public void saveProduct(ProductDto productDto) {
        Product product=new Product();

        product.setId(product.getId());
        product.setProductName(productDto.getProductName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());

        IProductRepository.save(product);

    }

    @Override
    public void deleteProduct(Integer id) {
        IProductRepository.deleteById(id);

    }

    @Override
    public void updateProduct(Integer id, ProductDto productDto) {
        Product product=IProductRepository.findById(id).orElse(null);
        if(product!=null){
            product.setProductName(productDto.getProductName());
            product.setStock(productDto.getStock());
            product.setId(productDto.getId());
            product.setPrice(productDto.getPrice());
            IProductRepository.save(product);

        }
    }

    @Override
    public List<ProductDto> getProducts() {
        List<ProductDto> productDtoList =IProductRepository.findAll()
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
    public ProductDto getProductById(Integer id) {
        Product product=IProductRepository.findById(id).orElse(null);

        ProductDto productDto=ProductDto.builder()
                .productName(product.getProductName())
                .stock(product.getStock())
                .price(product.getPrice())
                .id(product.getId())
                .build();

      return productDto;
    }
}

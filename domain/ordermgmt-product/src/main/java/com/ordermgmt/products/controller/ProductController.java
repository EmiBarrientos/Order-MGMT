package com.ordermgmt.products.controller;
import com.ordermgmt.products.dto.ProductDto;
import com.ordermgmt.products.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@RequestMapping("/api/product")
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id){
        ProductDto productDto=productService.getProductById(id);
        return ResponseEntity.ok(productDto);
    }


    @PostMapping
    public ResponseEntity<ProductDto> saveProduct(@Valid @RequestBody ProductDto productDto){
        productService.saveProduct(productDto);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok(Map.of("message", "Product Deleted Successfully"));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAllProducts(){
        List<ProductDto> productDtoList=productService.getProducts();
        return ResponseEntity.ok(productDtoList);
    }


    @PutMapping("{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto){
        productService.updateProduct(id, productDto);
        return ResponseEntity.ok(Map.of("message", "Product Updated Successfully"));
    }

    @PostMapping("/search")
     public ResponseEntity<List<ProductDto>> getProductsByIds(@RequestBody List<Long> ids) {
          if (ids == null || ids.isEmpty()) {
              return ResponseEntity.ok(Collections.emptyList());
          }

          if (ids.size() > 1000) {
             throw new IllegalArgumentException("Too many IDs. Maximum allowed is 1000.");
          }
          List<ProductDto> result = productService.findByIds(ids);

          return ResponseEntity.ok(result);
      }



}


/**
 *
 *
 *
 *
 *
 * */
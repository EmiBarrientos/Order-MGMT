package com.ordermgmt.products.controller;
import com.ordermgmt.products.dto.ProductDto;
import com.ordermgmt.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/product")
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/find/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Integer id){
        ProductDto productDto=productService.getProductById(id);
        return ResponseEntity.ok(productDto);
    }


    @PostMapping("/save")
    public ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto productDto){
        productService.saveProduct(productDto);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product Deleted Successfully");
    }

    @GetMapping("/findall")
    public ResponseEntity<List<ProductDto>> findAllProducts(){
        List<ProductDto> productDtoList=productService.getProducts();
        return ResponseEntity.ok(productDtoList);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody ProductDto productDto){
        productService.updateProduct(id, productDto);
        return ResponseEntity.ok("Product Updated Successfully");
    }

}

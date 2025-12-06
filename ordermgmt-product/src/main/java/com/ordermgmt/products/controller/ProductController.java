package com.ordermgmt.products.controller;
import com.ordermgmt.products.dto.ProductDto;
import com.ordermgmt.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
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

    @PostMapping("/find-by-ids")
     public ResponseEntity<List<ProductDto>> getProductsByIds(@RequestBody List<Long> ids) {
        System.out.println("Lista completa: " +ids);
          if (ids == null || ids.isEmpty()) {
              return ResponseEntity.ok(Collections.emptyList());
          }

          if (ids.size() > 1000) {
              return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
          }
          List<ProductDto> result = productService.findByIds(ids);
        System.out.println("Lista completa result: " +result.toString());
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
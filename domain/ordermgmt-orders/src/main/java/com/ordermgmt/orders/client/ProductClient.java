package com.ordermgmt.orders.client;

import com.ordermgmt.orders.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name= "ordermgmt-product", url="localhost:8080/api/product")
public interface ProductClient {
    @GetMapping("/find/{id}")
    ProductDto findProductById(@PathVariable Integer id);

    @PostMapping("/find-by-ids")
    List<ProductDto> getProductsByIds(@RequestBody List<Long> ids);




}


 /********************************
  *
 @GetMapping("/api/productos/find-by-ids", consumes = "application/json")
    List<ProductDto> findProductsByIds(@RequestBody List<Long> ids);


  @GetMapping("/findall")
  List<ProductDto> getProductsByIds(@RequestBody List<Long> ids);



  */

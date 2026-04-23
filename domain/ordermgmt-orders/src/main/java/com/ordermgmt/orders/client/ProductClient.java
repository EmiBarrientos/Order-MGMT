package com.ordermgmt.orders.client;

import com.ordermgmt.orders.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ordermgmt-product")
public interface ProductClient {

    @GetMapping("/api/product/find/{id}")
    ProductDto findProductById(@PathVariable Long id);

    @PostMapping("/api/product/search")
    List<ProductDto> getProductsByIds(@RequestBody List<Long> ids);
}


 /********************************
  *
 @GetMapping("/api/productos/find-by-ids", consumes = "application/json")
    List<ProductDto> findProductsByIds(@RequestBody List<Long> ids);


  @GetMapping("/findall")
  List<ProductDto> getProductsByIds(@RequestBody List<Long> ids);



  */

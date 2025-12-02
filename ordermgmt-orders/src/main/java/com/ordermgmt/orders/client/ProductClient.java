package com.ordermgmt.orders.client;

import com.ordermgmt.orders.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name= "ordermgmt-product", url="localhost:8080/api/product")
public interface ProductClient {
    @GetMapping("/find/{id}")
    ProductDto findProductById(@PathVariable Integer id);
}

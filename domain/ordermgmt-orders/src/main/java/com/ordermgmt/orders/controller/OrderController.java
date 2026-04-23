package com.ordermgmt.orders.controller;

import com.ordermgmt.orders.dto.OrderDto;
import com.ordermgmt.orders.dto.OrderOutputDto;
import com.ordermgmt.orders.dto.ProductDto;
import com.ordermgmt.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;




@RequestMapping("/api/order")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable Long id) {
        OrderDto orderDto = orderService.getOrderByid(id);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping
    public ResponseEntity<OrderDto> save(@RequestBody OrderDto orderDto) {
        orderService.saveOrder(orderDto);
        return ResponseEntity.ok(orderDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order Deleted Successfully");
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAll(){
        List<OrderDto> orderDtoList=orderService.getAllOrders();
        return ResponseEntity.ok(orderDtoList);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderDto orderDto){
        orderService.updateOrder(orderDto, id);
       return ResponseEntity.ok("Order Updated Successfully");
    }

    @GetMapping("/find-product-by-id/{idProduct}")
    public ResponseEntity<?> findProductById(@PathVariable Long idProduct){

        return ResponseEntity.ok(orderService.findProductById(idProduct));
    }

    @GetMapping("/with-products")
    public ResponseEntity<List<OrderOutputDto>> getAllWithProducts() {
        List<OrderOutputDto> result = orderService.getAllWhitProduct();
        return ResponseEntity.ok(result);

    }

}





/************************************************
 *
 * /* @GetMapping("findall-with-products")
 *     public ResponseEntity <List<OrderOutputDto>> getAllWithProducts(){
 *         List<OrderOutputDto> orderOutputDtoList=getAllOrderWithProducts();
 *         return ResponseEntity.ok(orderOutputDtoList);
 *     }
 *
 *
 *
 *
 * */
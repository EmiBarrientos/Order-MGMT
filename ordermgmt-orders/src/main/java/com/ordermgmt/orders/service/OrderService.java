package com.ordermgmt.orders.service;

import com.ordermgmt.orders.client.ProductClient;
import com.ordermgmt.orders.dto.OrderDto;
import com.ordermgmt.orders.dto.OrderOutputDto;
import com.ordermgmt.orders.dto.ProductDto;
import com.ordermgmt.orders.entity.Order;
import com.ordermgmt.orders.https.response.ProductByIdResponse;
import com.ordermgmt.orders.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService{

    private final IOrderRepository iOrderRepository;

    @Autowired
    private ProductClient productClient;

    public OrderService(IOrderRepository iOrderRepository, ProductClient productClient) {
        this.iOrderRepository = iOrderRepository;
        this.productClient = productClient;
    }

    @Override
    public void saveOrder(OrderDto orderDto) {
        Order order=new Order();
        order.setId(orderDto.getId());
        order.setEstadoPedido(orderDto.getEstadoPedido());
        order.setUserId(orderDto.getUserId());
        order.setIdProducto(orderDto.getIdProducto());

        iOrderRepository.save(order);

    }

    @Override
    public void updateOrder(OrderDto orderDto, Integer id) {
        Order order=iOrderRepository.findById(id).orElse(null);
        if(order!=null){
            order.setIdProducto(orderDto.getIdProducto());
            order.setEstadoPedido(orderDto.getEstadoPedido());
            order.setUserId(orderDto.getUserId());
            order.setId(orderDto.getId());
            iOrderRepository.save(order);
        }

    }

    @Override
    public void deleteOrder(Integer id) {
        iOrderRepository.deleteById(id);
    }

    @Override
    public OrderDto getOrderByid(Integer id) {
       Order order=iOrderRepository.findById(id).orElse(null);
       OrderDto orderDto= OrderDto.builder()
               .idProducto(order.getIdProducto())
               .userId(order.getUserId())
               .estadoPedido(order.getEstadoPedido())
               .id(order.getId())
               .build();

        return orderDto;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<OrderDto> orderDtoList=iOrderRepository.findAll()
                .stream()
                .map(order -> OrderDto.builder()
                        .userId(order.getUserId())
                        .idProducto(order.getIdProducto())
                        .estadoPedido(order.getEstadoPedido())
                        .id(order.getId())
                        .build())
                .toList();

        return orderDtoList;
    }

    @Override
    public ProductByIdResponse findProductById(Integer productId) {

        ProductDto productDto=productClient.findProductById(productId);

        return ProductByIdResponse.builder()
                .Price(productDto.getPrice())
                .productName(productDto.getProductName())
                .stock(productDto.getStock())
                .build();
    }

    @Override
    public List<OrderOutputDto> getAllWhitProduct() {
        List<Order> orders = iOrderRepository.findAll(); // o con paginado

        // 1) colectar todos los productIds
        List<Long> allProductIds = orders.stream()
                .flatMap(o -> o.getIdProducto().stream())
                .toList();

        System.out.println("Lista completa: " + allProductIds);

        if (allProductIds.isEmpty()) {

            return orders.stream()
                    .map(o -> new OrderOutputDto(o.getId(), o.getUserId(), Collections.emptyList(), o.getEstadoPedido()))
                    .collect(Collectors.toList());
        }

        List<ProductDto> products = productClient.getProductsByIds(allProductIds);
        System.out.println("lista productos desde de ir a pedidios: "+ products.toString());

        Map<Long, ProductDto> productMap = products.stream()
                .collect(Collectors.toMap(ProductDto::getId, Function.identity()));
        System.out.println("lista productosMap desde hacer el productos.stream: "+ productMap.toString());


        return orders.stream().map(o -> {
            List<ProductDto> pList = o.getIdProducto().stream()
                    .map(id -> productMap.get(id))
                    .filter(Objects::nonNull) 
                    .collect(Collectors.toList());
            System.out.println("lista PLIST desde cargar: "+ pList.toString());

            return new OrderOutputDto(o.getId(), o.getUserId(), pList, o.getEstadoPedido());
        }).collect(Collectors.toList());
    }

}





/******************************************************************************
 *
 *  public List<OrderOutputDto> getAllOdrderWhitProduct() {
 *
 *
 *            List<OrderOutputDto> orderOutputDtoList=iOrderRepository.findAll()
 *                    .stream()
 *                    .map(order -> OrderOutputDto.builder()
 *                            .userId(order.getUserId())
 *                            .id(order.getId())
 *                            .estadoPedido(order.getEstadoPedido())
 *                            .productDtoListDto(productClient.findProductsByIds())
 *
 *
 *                            .build())
 *                    .toList();
 *         return orderOutputDtoList;
 *     }
 *
 *
 *
 *
 *
 *
 *
 * **/
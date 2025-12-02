package com.ordermgmt.orders.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Integer id;
    private String productName;
    private Double stock;
    private Double price;
}

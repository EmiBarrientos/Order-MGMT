package com.ordermgmt.products.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private int id;
    private String productName;
    private Double stock;
    private Double price;
}

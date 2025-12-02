package com.ordermgmt.orders.https.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductByIdResponse {

    private String productName;
    private Double Price;
    private Double stock;


}

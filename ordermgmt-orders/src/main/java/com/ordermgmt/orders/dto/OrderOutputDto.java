package com.ordermgmt.orders.dto;

import com.ordermgmt.orders.enums.EstadoPedido;
import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderOutputDto {
    private int id;
    private Long userId;
    private List<ProductDto> productDtoListDto;
    private EstadoPedido estadoPedido;

}

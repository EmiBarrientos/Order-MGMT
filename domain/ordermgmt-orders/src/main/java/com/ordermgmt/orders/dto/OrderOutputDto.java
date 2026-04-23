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
    private Long id;
    private Long userId;
    private List<ProductDto> productDtoList;
    private EstadoPedido estadoPedido;

}

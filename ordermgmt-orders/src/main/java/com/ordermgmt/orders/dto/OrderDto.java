package com.ordermgmt.orders.dto;

import com.ordermgmt.orders.enums.EstadoPedido;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private int id;
    private Long userId;
    private List<Long> idProducto;
    private EstadoPedido estadoPedido;

}

package com.ordermgmt.orders.entity;

import com.ordermgmt.orders.enums.EstadoPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Long userId;
    private List<Long> idProducto;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estadoPedido;


}

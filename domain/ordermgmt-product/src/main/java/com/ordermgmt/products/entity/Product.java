package com.ordermgmt.products.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String productName;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

}

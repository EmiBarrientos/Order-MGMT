package com.ordermgmt.products.dto;
import jakarta.validation.constraints.*;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private int id;
    @NotBlank(message = "Product name is required")
    private String productName;
    @PositiveOrZero(message = "Stock must be zero or greater")
    private Double stock;
    @Positive(message = "Price must be greater than 0")
    private Double price;
}

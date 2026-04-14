package com.ordermgmt.products.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    @NotBlank(message = "Product name is required")
    private String productName;
    @PositiveOrZero(message = "Stock must be zero or greater")
    private Integer stock;
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;
}

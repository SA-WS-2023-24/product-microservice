package com.htwberlin.productservice.port.producer;

import com.htwberlin.productservice.core.domain.model.Category;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductMessage {
    private String productId;
    private String name;
    private BigDecimal price;
    private String imageLink;
    private String description;
    private Category category;
    private int quantity;
    private String basketId;
}

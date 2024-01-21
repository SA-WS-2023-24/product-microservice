package com.htwberlin.productservice.port.producer;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductMessage implements Serializable {
    private String productId;
    private String name;
    private BigDecimal price;
    private String imageLink;
    private String description;
    private int quantity;
    private String basketId;
}

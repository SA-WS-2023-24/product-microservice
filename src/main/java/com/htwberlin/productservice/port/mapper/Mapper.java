package com.htwberlin.productservice.port.mapper;

import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.port.producer.ProductMessage;

public class Mapper {
    public static ProductMessage productToProductMessage(Product product, int quantity, String basketId) {
        return ProductMessage.builder()
                .productId(product.getId().toString())
                .price(product.getPrice())
                .name(product.getName())
                .imageLink(product.getImageLink())
                .description(product.getDescription())
                .quantity(quantity)
                .basketId(basketId)
                .build();
    }
}

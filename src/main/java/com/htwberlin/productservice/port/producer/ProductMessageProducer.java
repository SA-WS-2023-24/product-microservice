package com.htwberlin.productservice.port.producer;

import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.impl.ProductService;
import com.htwberlin.productservice.port.mapper.Mapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("v1/basket/")
public class ProductMessageProducer {
    private final RabbitTemplate productTemplate;

    private final ProductService productService;

    public ProductMessageProducer(RabbitTemplate productTemplate, ProductService productService) {
        this.productTemplate = productTemplate;
        this.productService = productService;
    }

    @PostMapping("/{basketId}/add-to-basket")
    public ResponseEntity<?> publishAddToBasketEvent(@PathVariable String basketId, @RequestBody Map<String, Object> productJson) {
        String productId = productJson.get("productId").toString();
        int quantity = Integer.parseInt(productJson.get("quantity").toString());

        // if not found should return 404
        Product product = productService.getProduct(UUID.fromString(productId));

        ProductMessage productMessage = Mapper.productToProductMessage(product, quantity, basketId);

        // TODO add the exchange and the queue when rabbitmq is configured
        productTemplate.convertAndSend(productMessage);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{basketId}/update-basket")
    public ResponseEntity<?> publishUpdateBasketEvent(@PathVariable String basketId, @RequestBody Map<String, Object> jsonBody) {
        String productId = jsonBody.get("productId").toString();
        int quantity = Integer.parseInt(jsonBody.get("quantity").toString());

        Product product = productService.getProduct(UUID.fromString(productId));

        ProductMessage productMessage = ProductMessage
                .builder()
                .productId(productId)
                .quantity(quantity)
                .basketId(basketId)
                .build();

        productTemplate.convertAndSend(productMessage);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{basketId}/remove-from-basket")
    public ResponseEntity<?> publishRemoveFromBasketEvent(@PathVariable String basketId, @RequestBody Map<String, Object> jsonBody) {
        String productId = jsonBody.get("productId").toString();

        Product product = productService.getProduct(UUID.fromString(productId));

        ProductMessage productMessage = ProductMessage
                .builder()
                .productId(productId)
                .basketId(basketId)
                .build();

        productTemplate.convertAndSend(productMessage);

        return ResponseEntity.ok().build();
    }

}

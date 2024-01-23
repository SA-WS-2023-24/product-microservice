package com.htwberlin.productservice.port.producer;

import com.htwberlin.productservice.config.MQConfig.RabbitMQConfig;
import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.impl.ProductService;
import com.htwberlin.productservice.port.mapper.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("v1/basket/")
public class ProductMessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductMessageProducer.class);
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

        Product product = productService.getProduct(UUID.fromString(productId));

        ProductMessage productMessage = Mapper.productToProductMessage(product, quantity, basketId);

        LOGGER.info(String.format("Sending message -> %s", productMessage));

        productTemplate.convertAndSend(RabbitMQConfig.PRODUCT_EXCHANGE, "product.add", productMessage);

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

        LOGGER.info(String.format("Sending message -> %s", productMessage));

        productTemplate.convertAndSend(RabbitMQConfig.PRODUCT_EXCHANGE, "product.update", productMessage);

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

        LOGGER.info(String.format("Sending message -> %s", productMessage));

        productTemplate.convertAndSend(RabbitMQConfig.PRODUCT_EXCHANGE, "product.remove", productMessage);

        return ResponseEntity.ok().build();
    }

}

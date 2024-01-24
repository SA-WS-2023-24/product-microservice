package com.htwberlin.productservice.port.producer;

import com.htwberlin.productservice.config.MQConfig.RabbitMQConfig;
import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.interfaces.IProducer;
import com.htwberlin.productservice.port.mapper.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductMessageProducer implements IProducer {

    private final RabbitTemplate productTemplate;

    public ProductMessageProducer(RabbitTemplate productTemplate) {
        this.productTemplate = productTemplate;
    }


    @Override
    public void produceAddToBasketEvent(Product product, String basketId, int quantity) {
        ProductMessage productMessage = Mapper.productToProductMessage(product, quantity, basketId);

        log.info(String.format("Sending message -> %s", productMessage));

        productTemplate.convertAndSend(RabbitMQConfig.PRODUCT_EXCHANGE, "product.add", productMessage);
    }

    @Override
    public void produceUpdateBasketEvent(Product product, String basketId, int quantity) {
        ProductMessage productMessage = ProductMessage
                .builder()
                .productId(product.getId().toString())
                .quantity(quantity)
                .basketId(basketId)
                .build();

        log.info(String.format("Sending message -> %s", productMessage));

        productTemplate.convertAndSend(RabbitMQConfig.PRODUCT_EXCHANGE, "product.update", productMessage);
    }

    @Override
    public void produceRemoveFromBasketEvent(Product product, String basketId) {
        ProductMessage productMessage = ProductMessage.builder()
                .productId(product.getId().toString())
                .basketId(basketId)
                .build();

        log.info(String.format("Sending message REMOVE -> %s", productMessage));

        productTemplate.convertAndSend(RabbitMQConfig.PRODUCT_EXCHANGE, "product.remove", productMessage);
    }
}

package com.htwberlin.productservice.port.producer;

import com.htwberlin.productservice.config.MQConfig.RabbitMQConfig;
import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.interfaces.IProducer;
import com.htwberlin.productservice.port.mapper.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductMessageProducer implements IProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductMessageProducer.class);
    private final RabbitTemplate productTemplate;

    public ProductMessageProducer(RabbitTemplate productTemplate) {
        this.productTemplate = productTemplate;
    }


    @Override
    public void produceAddToBasketEvent(Product product, String basketId, int quantity) {
        ProductMessage productMessage = Mapper.productToProductMessage(product, quantity, basketId);

        LOGGER.info(String.format("Sending message -> %s", productMessage));

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

        LOGGER.info(String.format("Sending message -> %s", productMessage));

        productTemplate.convertAndSend(RabbitMQConfig.PRODUCT_EXCHANGE, "product.update", productMessage);
    }

    @Override
    public void produceRemoveFromBasketEvent(Product product, String basketId) {
        ProductMessage productMessage = ProductMessage.builder()
                .productId(product.getId().toString())
                .basketId(basketId)
                .build();

        LOGGER.info(String.format("Sending message REMOVE -> %s", productMessage));

        productTemplate.convertAndSend(RabbitMQConfig.PRODUCT_EXCHANGE, "product.remove", productMessage);
    }
}

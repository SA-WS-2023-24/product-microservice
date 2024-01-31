package com.htwberlin.productservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ADD_PRODUCT_QUEUE = "add_product_queue";
    public static final String UPDATE_PRODUCT_QUEUE = "update_product_queue";
    public static final String REMOVE_PRODUCT_QUEUE = "remove_product_queue";
    public static final String PRODUCT_EXCHANGE = "product_exchange";

    @Bean
    public Queue addProductQueue() {
        return new Queue(ADD_PRODUCT_QUEUE, false);
    }
    @Bean
    public Queue updateProductQueue() {
        return new Queue(UPDATE_PRODUCT_QUEUE, false);
    }
    @Bean
    public Queue removeProductQueue() {
        return new Queue(REMOVE_PRODUCT_QUEUE, false);
    }
    @Bean
    public TopicExchange productTopicExchange() {
        return new TopicExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Binding bindAddProductQueue(TopicExchange exchange) {
        return BindingBuilder.bind(addProductQueue()).to(exchange).with("product.add");
    }


    @Bean
    public Binding bindUpdateProductQueue(TopicExchange exchange) {
        return BindingBuilder.bind(updateProductQueue()).to(exchange).with("product.update");
    }


    @Bean
    public Binding bindRemoveProductQueue(TopicExchange exchange) {
        return BindingBuilder.bind(removeProductQueue()).to(exchange).with("product.remove");
    }

    @Bean
    public RabbitTemplate productTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}

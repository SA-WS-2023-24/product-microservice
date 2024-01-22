package com.htwberlin.productservice.core.domain.service.impl;

import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.exception.ProductNotFoundException;
import com.htwberlin.productservice.core.domain.service.interfaces.IBasketService;
import com.htwberlin.productservice.core.domain.service.interfaces.IProducer;
import com.htwberlin.productservice.core.domain.service.interfaces.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BasketService implements IBasketService {

    private final IProducer producer;
    private final IProductRepository productRepository;

    public BasketService(IProducer producer, IProductRepository productRepository) {
        this.producer = producer;
        this.productRepository = productRepository;
    }

    @Override
    public void addToBasket(String basketId, String productId, int quantity) throws ProductNotFoundException {
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new ProductNotFoundException(UUID.fromString(productId)));

        producer.produceAddToBasketEvent(product, basketId, quantity);
    }

    @Override
    public void updateBasket(String basketId, String productId, int quantity) throws ProductNotFoundException {
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new ProductNotFoundException(UUID.fromString(productId)));

        producer.produceUpdateBasketEvent(product,basketId,quantity);
    }

    @Override
    public void removeFromBasket(String basketId, String productId) throws ProductNotFoundException {
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new ProductNotFoundException(UUID.fromString(productId)));

        producer.produceRemoveFromBasketEvent(product, basketId);
    }
}

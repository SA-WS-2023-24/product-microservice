package com.htwberlin.productservice.core.domain.service.interfaces;

import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.exception.ProductNotFoundException;

public interface IProducer {
    void produceAddToBasketEvent(Product product, String basketId, int quantity) throws ProductNotFoundException;

    void produceUpdateBasketEvent(Product product, String basketId, int quantity) throws ProductNotFoundException;

    void produceRemoveFromBasketEvent(Product product, String basketId) throws ProductNotFoundException;
}

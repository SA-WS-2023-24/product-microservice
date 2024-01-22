package com.htwberlin.productservice.core.domain.service.interfaces;

public interface IBasketService {

    void addToBasket(String basketId, String productId, int quantity);

    void updateBasket(String basketId, String productId, int quantity);

    void removeFromBasket(String basketId, String productId);
}

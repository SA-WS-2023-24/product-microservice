package com.example.demo.core.domain.service.interfaces;

import com.example.demo.core.domain.model.Product;
import com.example.demo.core.domain.service.exception.ProductNotFoundException;

import java.util.UUID;

public interface IProductService
{
    Product createProduct (Product product);

    Product updateProduct (Product product) throws ProductNotFoundException;

    void deleteProduct (Product product);

    Product getProduct(UUID id) throws ProductNotFoundException;

    Iterable<Product> getAllProducts();
}

package com.htwberlin.productservice.core.domain.service.interfaces;

import com.htwberlin.productservice.core.domain.model.Category;
import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.exception.ProductNotFoundException;

import java.util.UUID;

public interface IProductService
{
    Product createProduct (Product product);

    Product updateProduct (Product product) throws ProductNotFoundException;

    void deleteProduct (UUID id);

    Product getProduct(UUID id) throws ProductNotFoundException;

    Iterable<Product> getAllProducts();

    Iterable<Product> getProductsByKeyword(String keyword);

    Iterable<Product> getProductsByCategory(Category category);
}

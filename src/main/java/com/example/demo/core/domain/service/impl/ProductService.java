package com.example.demo.core.domain.service.impl;

import com.example.demo.core.domain.model.Product;
import com.example.demo.core.domain.service.exception.ProductNotFoundException;
import com.example.demo.core.domain.service.interfaces.IProductRepository;
import com.example.demo.core.domain.service.interfaces.IProductService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService implements IProductService {

    private final IProductRepository productRepository;

    ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) throws ProductNotFoundException {
        if (!productRepository.existsById(product.getId())) {
            throw new ProductNotFoundException(product.getId());
        }
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    @Override
    public Product getProduct(UUID id) throws ProductNotFoundException {
        Optional<Product> retrievedProduct = productRepository.findById(id);
        return retrievedProduct.orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

}

package com.htwberlin.productservice.core.domain.service.impl;

import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.exception.ProductNotFoundException;
import com.htwberlin.productservice.core.domain.service.interfaces.IProductRepository;
import com.htwberlin.productservice.core.domain.service.interfaces.IProductService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService implements IProductService {

    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @CachePut(cacheNames = "productCache", key = "#product.id")
    public Product updateProduct(Product product) throws ProductNotFoundException {
        if (!productRepository.existsById(product.getId())) {
            throw new ProductNotFoundException(product.getId());
        }
        return productRepository.save(product);
    }

    @Override
    @CacheEvict(cacheNames = {"productCache", "productsCache"}, key = "#product.id", beforeInvocation = true)
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    @Override
    @Cacheable(value = "productCache", key = "#id")
    public Product getProduct(UUID id) throws ProductNotFoundException {
        Optional<Product> retrievedProduct = productRepository.findById(id);
        return retrievedProduct.orElseThrow(() -> new ProductNotFoundException(id));
    }


    @Override
    @Cacheable(value = "productsCache", key = "'allProducts'")
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Iterable<Product> getProductsByKeyword(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}

package com.htwberlin.productservice.core.domain.service.impl;

import com.htwberlin.productservice.core.domain.model.Category;
import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.exception.ProductNotFoundException;
import com.htwberlin.productservice.core.domain.service.interfaces.IProductRepository;
import com.htwberlin.productservice.core.domain.service.interfaces.IProductService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    @CachePut(value = {"productCache", "allProductsCache"}, key = "#product.id")
    public Product updateProduct(Product product) throws ProductNotFoundException {
        if (!productRepository.existsById(product.getId())) {
            throw new ProductNotFoundException(product.getId());
        }
        return productRepository.save(product);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "allProductsCache", allEntries = true),
            @CacheEvict(value = "productCache", key = "#product.id"),
            @CacheEvict(value = "categoryFilter", key = "#product.getCategory().name().toLowerCase()")
    })
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
    @Cacheable(value = "allProductsCache", key = "'allProducts'")
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Cacheable(value = "allProductsCache", key = "#keyword")
    public Iterable<Product> getProductsByKeyword(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    @Cacheable(value = "categoryFilter", key = "#category.name().toLowerCase()")
    public Iterable<Product> getProductsByCategory(Category category) {
        return productRepository.findAllByCategory(category);
    }
}

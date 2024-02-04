package com.htwberlin.productservice.core.domain.service.impl;

import com.htwberlin.productservice.core.domain.model.Category;
import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.exception.ProductNotFoundException;
import com.htwberlin.productservice.core.domain.service.interfaces.IProductRepository;
import com.htwberlin.productservice.core.domain.service.interfaces.IProductService;
import org.springframework.cache.annotation.CacheEvict;
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

    @Override
    @CacheEvict(value = {"products_all", "products_id", "products_search", "products_category"}, allEntries = true)
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @CacheEvict(value = {"products_all", "products_id", "products_search", "products_category"}, allEntries = true)
    public Product updateProduct(Product product) throws ProductNotFoundException {
        if (!productRepository.existsById(product.getId())) {
            throw new ProductNotFoundException(product.getId());
        }
        return productRepository.save(product);
    }

    @Override
    @CacheEvict(value = {"products_all", "products_id", "products_search", "products_category"}, allEntries = true)
    public void deleteProduct(UUID id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

    @Override
    @Cacheable(value = "products_id", key = "#id")
    public Product getProduct(UUID id) throws ProductNotFoundException {
        Optional<Product> retrievedProduct = productRepository.findById(id);
        return retrievedProduct.orElseThrow(() -> new ProductNotFoundException(id));
    }


    @Override
    @Cacheable(value = "products_all")
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Cacheable(value = "products_search", key = "#keyword")
    public Iterable<Product> getProductsByKeyword(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    @Cacheable(value = "products_category", key = "#category.name().toLowerCase()")
    public Iterable<Product> getProductsByCategory(Category category) {
        return productRepository.findAllByCategory(category);
    }
}

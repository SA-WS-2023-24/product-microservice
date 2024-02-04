package com.htwberlin.productservice.core.domain.service.interfaces;

import com.htwberlin.productservice.core.domain.model.Category;
import com.htwberlin.productservice.core.domain.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IProductRepository extends CrudRepository<Product, UUID> {

    Iterable<Product> findByNameContainingIgnoreCase(String keyword);

    Iterable<Product> findAllByCategory(Category category);
}



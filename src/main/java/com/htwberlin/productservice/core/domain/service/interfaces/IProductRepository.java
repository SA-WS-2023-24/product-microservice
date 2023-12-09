package com.htwberlin.productservice.core.domain.service.interfaces;

import com.htwberlin.productservice.core.domain.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface IProductRepository extends CrudRepository<Product, UUID> {

}



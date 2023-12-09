package com.example.demo.core.domain.service.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(UUID id) {
      super("Could not find product " + id.toString());
    }
}

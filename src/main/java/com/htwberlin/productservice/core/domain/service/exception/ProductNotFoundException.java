package com.htwberlin.productservice.core.domain.service.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(UUID id) {
      super("Could not find product " + id.toString());
    }
}

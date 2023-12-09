package com.htwberlin.productservice.user.controller;

import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.interfaces.IProductService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping(path = "/product")
    public @ResponseBody Product create(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable UUID id) {
        return productService.getProduct(id);
    }

    @PutMapping(path = "/product")
    public @ResponseBody Product update(Product product) {
        return productService.updateProduct(product);
    }

    @DeleteMapping(path = "/product/{id}")
    public @ResponseBody void delete(@RequestBody Product product) {
        productService.deleteProduct(product);
    }

    @GetMapping("/products")
    public @ResponseBody Iterable<Product> getProducts() {
        return productService.getAllProducts();
    }
}

package com.htwberlin.productservice.port.user;

import com.htwberlin.productservice.core.domain.model.Category;
import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.interfaces.IProductService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/")
@CrossOrigin(origins = "*")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping(path = "/product")
    public @ResponseBody Product create(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @GetMapping("/product/search")
    public @ResponseBody Iterable<Product> getByKeyword(@RequestParam String keyword) {
        return productService.getProductsByKeyword(keyword);
    }

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable UUID id) {
        return productService.getProduct(id);
    }

    @PutMapping(path = "/product")
    public @ResponseBody Product update(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @DeleteMapping(path = "/product/{id}")
    public @ResponseBody void delete(@PathVariable UUID id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/products")
    public @ResponseBody Iterable<Product> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/filter")
    public @ResponseBody Iterable<Product> getByCategory(@RequestParam String category) {
        Category cat = Category.valueOf(category.toUpperCase());
        return productService.getProductsByCategory(cat);
    }
}

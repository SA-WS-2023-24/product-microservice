package com.htwberlin.productservice.port.user;

import com.htwberlin.productservice.core.domain.service.interfaces.IBasketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("v1/basket")
public class ProductMessageController {

    private final IBasketService basketService;

    public ProductMessageController(IBasketService basketService) {
        this.basketService = basketService;
    }

     @PostMapping("/{basketId}/add-to-basket")
    public ResponseEntity<?> publishAddToBasketEvent(@PathVariable String basketId, @RequestBody Map<String, Object> productJson) {
         String productId = productJson.getOrDefault("productId", "").toString();
         int quantity = Integer.parseInt(productJson.getOrDefault("quantity", 0).toString());

        basketService.addToBasket(basketId, productId, quantity);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{basketId}/update-basket")
    public ResponseEntity<?> publishUpdateBasketEvent(@PathVariable String basketId, @RequestBody Map<String, Object> productJson) {
        String productId = productJson.getOrDefault("productId", "").toString();
        int quantity = Integer.parseInt(productJson.getOrDefault("quantity", 0).toString());

        basketService.updateBasket(basketId, productId, quantity);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{basketId}/remove-from-basket")
    public ResponseEntity<?> publishRemoveFromBasketEvent(@PathVariable String basketId, @RequestBody Map<String, Object> productJson) {
        String productId = productJson.getOrDefault("productId", "").toString();

        basketService.removeFromBasket(basketId, productId);

        return ResponseEntity.ok().build();
    }

}

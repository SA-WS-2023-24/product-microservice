package com.htwberlin.productservice.port.user;

import com.htwberlin.productservice.core.domain.model.Review;
import com.htwberlin.productservice.core.domain.service.interfaces.IReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/")
public class ReviewController {


    private final IReviewService reviewService;

    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/review")
    public @ResponseBody Review create(@RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @GetMapping("/reviews/{productId}")
    public @ResponseBody Iterable<Review> getAllReviews(@PathVariable UUID productId) {
        return reviewService.getAllReviewsForProduct(productId);
    }

    @DeleteMapping("/reviews/{id}")
    public @ResponseBody void deleteReview(@RequestBody Review review) {
        reviewService.deleteReview(review);
    }
}

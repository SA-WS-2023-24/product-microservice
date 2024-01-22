package com.htwberlin.productservice.core.domain.service.impl;

import com.htwberlin.productservice.core.domain.model.Review;
import com.htwberlin.productservice.core.domain.service.interfaces.IReviewRepository;
import com.htwberlin.productservice.core.domain.service.interfaces.IReviewService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewService implements IReviewService {

    private final IReviewRepository reviewRepository;

    public ReviewService(IReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    @Override
    @Cacheable(value = "reviewsCache", key = "#review.id")
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    @CacheEvict(value = {"reviewsCache", "reviewsForProduct"}, key = "#review.id", allEntries = true)
    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }

    @Override
    @Cacheable(value = "reviewsForProduct", key = "productId")
    public Iterable<Review> getAllReviewsForProduct(UUID productId) {
        return reviewRepository.findAllByProductId(productId);
    }
}

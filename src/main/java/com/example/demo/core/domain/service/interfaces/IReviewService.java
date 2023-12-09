package com.example.demo.core.domain.service.interfaces;

import com.example.demo.core.domain.model.Review;

import java.util.UUID;

public interface IReviewService {
    Review createReview(Review review);

    void deleteReview(Review review);

    Iterable<Review> getAllReviewsForProduct(UUID id);
}
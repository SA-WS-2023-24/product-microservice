package com.htwberlin.productservice.core.domain.service.interfaces;

import com.htwberlin.productservice.core.domain.model.Review;

import java.util.UUID;

public interface IReviewService {
    Review createReview(Review review);

    void deleteReview(Review review);

    Iterable<Review> getAllReviewsForProduct(UUID id);
}
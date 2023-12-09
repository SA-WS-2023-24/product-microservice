package com.example.demo.core.domain.service.interfaces;

import com.example.demo.core.domain.model.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface IReviewRepository extends CrudRepository<Review, UUID> {

    Iterable<Review> findAllByProductId(UUID productId);

}

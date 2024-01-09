package com.htwberlin.productservice;

import com.htwberlin.productservice.core.domain.model.Category;
import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.model.Review;
import com.htwberlin.productservice.core.domain.service.interfaces.IProductRepository;
import com.htwberlin.productservice.core.domain.service.interfaces.IReviewRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Random;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final IProductRepository repository;
    private final IReviewRepository reviews;

    public DataLoader(IProductRepository repository, IReviewRepository reviews) {
        this.repository = repository;
        this.reviews = reviews;
    }

    @Override
    public void run(String... args) throws Exception {
        Category[] categories = Category.values();

        for (int i = 0; i < 20; i++) {
            int randomIndex = new Random().nextInt(Category.values().length);
            Product product = Product.builder()
                    .id(UUID.randomUUID())
                    .category(categories[randomIndex])
                    .name("Dummy Product" + i)
                    .description("Dummy product description")
                    .imageLink("http://dummyImageLinke.com")
                    .price(new BigDecimal(250 + i))
                    .build();

            Product productForReview = this.repository.save(product);

            this.repository.findAll().forEach(System.out::println);

            if (new Random().nextBoolean()) {
                int year = new Random().nextInt(2024);
                Month[] months = Month.values();
                Month month = months[new Random().nextInt(months.length)];
                int day = new Random().nextInt(28) + 1;
                int hour = new Random().nextInt(24);
                int minute = new Random().nextInt(60);

                Review review = Review.builder()
                        .id(UUID.randomUUID())
                        .userId(UUID.randomUUID())
                        .content("Important review")
                        .stars(new Random().nextInt(5))
                        .publishedDate(LocalDateTime.of(year, month, day, hour,
                                minute)).product(productForReview).build();

                System.out.println(review);

                this.reviews.save(review);
            }
        }
    }
}

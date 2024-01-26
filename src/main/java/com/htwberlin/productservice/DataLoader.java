package com.htwberlin.productservice;

import com.htwberlin.productservice.core.domain.model.Category;
import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.model.Review;
import com.htwberlin.productservice.core.domain.service.interfaces.IProductRepository;
import com.htwberlin.productservice.core.domain.service.interfaces.IReviewRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final IProductRepository productRepository;
    private final IReviewRepository reviewRepository;

    public DataLoader(IProductRepository productRepository, IReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        List<Product> computerParts = Arrays.asList(
                Product.builder()
                        .name("AMD Ryzen 9 5900X CPU")
                        .price(new BigDecimal(549))
                        .category(Category.CPU)
                        .imageLink("https://media2.nbb-cdn.de/images/products/originals/AMD_Ryzen_9_5000_Series_01_908e.jpg?size=400")
                        .description("High-performance 12-core processor for gaming and multitasking.")
                        .build(),

                Product.builder()
                        .name("NVIDIA GeForce RTX 3070 Ti GPU")
                        .price(new BigDecimal(799))
                        .category(Category.VIDEOCARD)
                        .imageLink("https://media2.nbb-cdn.de/images/products/originals/GeForce_RTX_3070_Ti_VENTUS_3X_8G_OC_box-card_2d9e.png?size=400")
                        .description("Powerful graphics card for smooth gaming and graphics-intensive tasks.")
                        .build(),

                Product.builder()
                        .name("Corsair Vengeance LPX DDR4 RAM")
                        .price(new BigDecimal(129))
                        .category(Category.RAM)
                        .imageLink("https://media2.nbb-cdn.de/images/products/originals/Corsair-Vengeance-LPX-DDR4-DIMM-Black-2er-Kit_25_de43.jpg?size=400")
                        .description("Reliable and affordable DDR4 RAM for improved system performance.")
                        .build(),

                Product.builder()
                        .name("Samsung 970 EVO Plus NVMe SSD")
                        .price(new BigDecimal(159))
                        .category(Category.STORAGE)
                        .imageLink("https://media2.nbb-cdn.de/images/products/originals/Samsung_NVMe-970-EVO-Plus_1TB_Front_1e0c.jpg?size=400")
                        .description("Fast NVMe SSD with large storage capacity for quick data access.")
                        .build(),

                Product.builder()
                        .name("ASUS ROG Crosshair VIII Hero Motherboard")
                        .price(new BigDecimal(399))
                        .category(Category.MOTHERBOARD)
                        .imageLink("https://media2.nbb-cdn.de/images/products/originals/X670E_Hero_01-1_41c4.png?size=400")
                        .description("High-end motherboard with premium features for gaming enthusiasts.")
                        .build(),

                Product.builder()
                        .name("NZXT Kraken X73 Liquid Cooler")
                        .price(new BigDecimal(179))
                        .category(Category.COOLING)
                        .imageLink("https://media2.nbb-cdn.de/images/products/originals/NZXT_Kraken_X63_RGB_01_e7dd.jpg?size=400")
                        .description("Efficient liquid cooling solution for optimal CPU temperature.")
                        .build(),

                Product.builder()
                        .name("Western Digital Black 1TB")
                        .price(new BigDecimal(79))
                        .category(Category.STORAGE)
                        .imageLink("https://media2.nbb-cdn.de/images/products/originals/WestDig_WD-Black_mobile_Game-1TB_Hero_81df.jpg?size=400")
                        .description("High-capacity HDD for additional storage needs.")
                        .build(),

                Product.builder()
                        .name("Logitech G Pro X Mechanical Keyboard")
                        .price(new BigDecimal(149))
                        .category(Category.PERIPHERAL)
                        .imageLink("https://resource.logitechg.com/w_692,c_lpad,ar_4:3,q_auto,f_auto,dpr_1.0/d_transparent.gif/content/dam/gaming/en/products/pro-x-keyboard/pro-x-keyboard-gallery-1.png?v=1")
                        .description("Compact and customizable mechanical keyboard for gaming.")
                        .build(),

                Product.builder()
                        .name("SteelSeries Rival 600 Gaming Mouse")
                        .price(new BigDecimal(79))
                        .category(Category.PERIPHERAL)
                        .imageLink("https://www.notebooksbilliger.de/steelseries+aerox+3+onyx+wireless+ultraleichte+gaming+maus2022+743654")
                        .description("Precision gaming mouse with customizable features for competitive play.")
                        .build(),

                Product.builder()
                        .name("EVGA SuperNOVA 850 G5 Power Supply")
                        .price(new BigDecimal(129))
                        .category(Category.POWER_SUPPLY)
                        .imageLink("https://www.notebooksbilliger.de/steelseries+aerox+3+onyx+wireless+ultraleichte+gaming+maus2022+743654")
                        .description("High-efficiency power supply unit for stable and reliable performance.")
                        .build()

        );

        Iterable<Product> partsInDb = productRepository.saveAll(computerParts);

        Iterator<Product> dbIterator = partsInDb.iterator();

        for (int i = 0; i < 10 && dbIterator.hasNext(); i++) {
            Product currentProduct = dbIterator.next();
            Review review = generateReview(currentProduct);
            if (i % 2 == 0) {
                Review secondReview = generateReview(currentProduct);
                reviewRepository.save(secondReview);
            }
            reviewRepository.save(review);
        }
    }

    private LocalDateTime randomDate() {

        int year = new Random().nextInt(2024);
        Month[] months = Month.values();
        Month month = months[new Random().nextInt(months.length)];
        int day = new Random().nextInt(28) + 1;
        int hour = new Random().nextInt(24);
        int minute = new Random().nextInt(60);

        return LocalDateTime.of(year, month, day, hour, minute);
    }

    private Review generateReview(Product product) {
        int stars = (int) (Math.random() * 5) + 1;
        String[] reviewContents = {
                "Excellent product! Highly recommended.",
                "Good performance and value for money.",
                "Average product, does the job.",
                "Not satisfied with the quality.",
                "Outstanding! The best computer part I've ever used."
        };

        String content = reviewContents[(int) (Math.random() * reviewContents.length)];

        return Review.builder()
                .stars(stars)
                .product(product)
                .content(content)
                .publishedDate(randomDate())
                .productId(product.getId())
                .build();
    }
}

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
                        .imageLink("https://www.amd.com/system/files/styles/992px/private/2020-09/616656-amd-ryzen-9-5000-series-PIB-1260x709_0.png?itok=flFMuxbT")
                        .description("High-performance 12-core processor for gaming and multitasking.")
                        .build(),

                Product.builder()
                        .name("NVIDIA GeForce RTX 3070 Ti GPU")
                        .price(new BigDecimal(799))
                        .category(Category.VIDEOCARD)
                        .imageLink("https://asset.msi.com/resize/image/global/product/product_1622528277a4c1902fbb975eefeda64faddf79653e.png62405b38c58fe0f07fcef2367d8a9ba1/1024.png")
                        .description("Powerful graphics card for smooth gaming and graphics-intensive tasks.")
                        .build(),

                Product.builder()
                        .name("Corsair Vengeance LPX DDR4 RAM")
                        .price(new BigDecimal(129))
                        .category(Category.RAM)
                        .imageLink("https://assets.corsair.com/image/upload/c_pad,q_auto,h_1024,w_1024,f_auto/products/Memory/CMK16GX4M2B3000C15/Gallery/VENG_LPX_BLK_01.webp?width=1080&quality=85&auto=webp&format=pjpg")
                        .description("Reliable and affordable DDR4 RAM for improved system performance.")
                        .build(),

                Product.builder()
                        .name("Samsung 970 EVO Plus NVMe SSD")
                        .price(new BigDecimal(159))
                        .category(Category.STORAGE)
                        .imageLink("https://images.samsung.com/is/image/samsung/p6pim/de/mz-v7s2t0bw/gallery/de-970-evo-plus-nvme-m2-ssd-mz-v7s2t0bw-503979813?$650_519_PNG$")
                        .description("Fast NVMe SSD with large storage capacity for quick data access.")
                        .build(),

                Product.builder()
                        .name("ASUS ROG Crosshair VIII Hero Motherboard")
                        .price(new BigDecimal(399))
                        .category(Category.MOTHERBOARD)
                        .imageLink("https://dlcdnwebimgs.asus.com/gain/71525821-7155-46D1-94E2-ED3E3C5BDD61/w1000/h732")
                        .description("High-end motherboard with premium features for gaming enthusiasts.")
                        .build(),

                Product.builder()
                        .name("NZXT Kraken X73 Liquid Cooler")
                        .price(new BigDecimal(179))
                        .category(Category.COOLING)
                        .imageLink("https://www.datocms-assets.com/34299/1615585055-kraken-x73frontbnwith-fanpurple.png")
                        .description("Efficient liquid cooling solution for optimal CPU temperature.")
                        .build(),

                Product.builder()
                        .name("Western Digital Black 1TB")
                        .price(new BigDecimal(79))
                        .category(Category.STORAGE)
                        .imageLink("https://www.westerndigital.com/content/dam/store/en-us/assets/products/internal-storage/wd-black-desktop-sata-hdd/gallery/wd-black-desktop-500gb.png.wdthumb.1280.1280.webp")
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
                        .imageLink("https://media.steelseriescdn.com/thumbs/filer_public/a8/b6/a8b63d4f-f79f-406c-9c05-1d34ca44fbaf/03_rival600_kv_dual_hero.png__1850x800_crop-scale_optimize_subsampling-2.webp")
                        .description("Precision gaming mouse with customizable features for competitive play.")
                        .build(),

                Product.builder()
                        .name("EVGA SuperNOVA 850 G5 Power Supply")
                        .price(new BigDecimal(129))
                        .category(Category.POWER_SUPPLY)
                        .imageLink("https://images.evga.com/products/gallery//png/220-G5-0850-X2_LG_1.png")
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

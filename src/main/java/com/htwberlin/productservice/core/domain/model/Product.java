package com.htwberlin.productservice.core.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private UUID id;

    private String name;

    private BigDecimal price;

    private String imageLink;

    @Column(name = "description", length = 2500)
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;
}

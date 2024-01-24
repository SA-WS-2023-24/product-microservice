package com.htwberlin.productservice.core.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    private String content;
    private int stars;
    private LocalDateTime publishedDate;
    private UUID productId;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "product_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Product product;
}

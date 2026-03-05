package com.appointly.reviewservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // Customer who wrote the review

    @Column(nullable = false)
    private Long businessId;

    private Long serviceId; // Optional - specific service being reviewed

    @Column(nullable = false)
    private Long appointmentId; // Link to the appointment

    @Column(nullable = false)
    private Integer rating; // 1-5 stars

    @Column(length = 1000)
    private String comment;

    private String title;

    @Column(nullable = false)
    private Boolean isVerifiedPurchase = false;

    @Column(nullable = false)
    private Boolean isPublished = true;

    private Integer helpfulCount = 0;

    private Integer notHelpfulCount = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
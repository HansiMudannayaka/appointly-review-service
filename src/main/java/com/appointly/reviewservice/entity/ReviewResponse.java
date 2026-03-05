package com.appointly.reviewservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(nullable = false)
    private Long staffId; // Staff member who responded

    @Column(nullable = false)
    private String responseMessage;

    @CreationTimestamp
    private LocalDateTime respondedAt;

    private LocalDateTime updatedAt;
}
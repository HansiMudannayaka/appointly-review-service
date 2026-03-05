package com.appointly.reviewservice.repository;

import com.appointly.reviewservice.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByBusinessIdAndIsPublishedTrue(Long businessId, Pageable pageable);

    Page<Review> findByUserId(Long userId, Pageable pageable);

    Page<Review> findByServiceIdAndIsPublishedTrue(Long serviceId, Pageable pageable);

    Optional<Review> findByAppointmentId(Long appointmentId);

    boolean existsByAppointmentId(Long appointmentId);

    List<Review> findByBusinessIdAndRating(Long businessId, Integer rating);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.businessId = :businessId AND r.isPublished = true")
    Double getAverageRatingByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.businessId = :businessId AND r.isPublished = true")
    Long countByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.businessId = :businessId AND r.isPublished = true GROUP BY r.rating")
    List<Object[]> getRatingDistribution(@Param("businessId") Long businessId);
}
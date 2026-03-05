package com.appointly.reviewservice.service;

import com.appointly.reviewservice.dto.request.CreateReviewRequest;
import com.appointly.reviewservice.dto.response.BusinessRatingSummary;
import com.appointly.reviewservice.dto.response.ReviewResponseDTO;
import com.appointly.reviewservice.entity.Review;
import com.appointly.reviewservice.entity.ReviewResponse;
import com.appointly.reviewservice.exception.DuplicateReviewException;
import com.appointly.reviewservice.exception.ReviewNotFoundException;
import com.appointly.reviewservice.repository.ReviewRepository;
import com.appointly.reviewservice.repository.ReviewResponseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewResponseRepository responseRepository;

    @Transactional
    public ReviewResponseDTO createReview(CreateReviewRequest request) {
        log.info("Creating review for appointment: {}", request.getAppointmentId());

        // Check if review already exists for this appointment
        if (reviewRepository.existsByAppointmentId(request.getAppointmentId())) {
            throw new DuplicateReviewException("A review already exists for this appointment");
        }

        Review review = new Review();
        review.setUserId(request.getUserId());
        review.setBusinessId(request.getBusinessId());
        review.setServiceId(request.getServiceId());
        review.setAppointmentId(request.getAppointmentId());
        review.setRating(request.getRating());
        review.setTitle(request.getTitle());
        review.setComment(request.getComment());
        review.setIsVerifiedPurchase(true); // You might want to verify this with appointment service
        review.setIsPublished(true);

        Review savedReview = reviewRepository.save(review);
        log.info("Review created with ID: {}", savedReview.getId());

        return mapToDTO(savedReview);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDTO> getReviewsByBusinessId(Long businessId, Pageable pageable) {
        log.info("Fetching reviews for business: {}", businessId);
        return reviewRepository.findByBusinessIdAndIsPublishedTrue(businessId, pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDTO> getReviewsByUserId(Long userId, Pageable pageable) {
        log.info("Fetching reviews by user: {}", userId);
        return reviewRepository.findByUserId(userId, pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public ReviewResponseDTO getReviewById(Long id) {
        log.info("Fetching review with ID: {}", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + id));
        return mapToDTO(review);
    }

    @Transactional
    public ReviewResponseDTO updateReview(Long id, CreateReviewRequest request) {
        log.info("Updating review with ID: {}", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + id));

        review.setRating(request.getRating());
        review.setTitle(request.getTitle());
        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);
        return mapToDTO(updatedReview);
    }

    @Transactional
    public void deleteReview(Long id) {
        log.info("Deleting review with ID: {}", id);
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException("Review not found with ID: " + id);
        }
        reviewRepository.deleteById(id);
    }

    @Transactional
    public ReviewResponseDTO markHelpful(Long id) {
        log.info("Marking review {} as helpful", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + id));
        review.setHelpfulCount(review.getHelpfulCount() + 1);
        return mapToDTO(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public BusinessRatingSummary getBusinessRatingSummary(Long businessId) {
        log.info("Generating rating summary for business: {}", businessId);

        Double averageRating = reviewRepository.getAverageRatingByBusinessId(businessId);
        Long totalReviews = reviewRepository.countByBusinessId(businessId);

        List<Object[]> distribution = reviewRepository.getRatingDistribution(businessId);
        Map<Integer, Long> ratingCounts = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            ratingCounts.put(i, 0L);
        }

        for (Object[] row : distribution) {
            Integer rating = ((Number) row[0]).intValue();
            Long count = ((Number) row[1]).longValue();
            ratingCounts.put(rating, count);
        }

        return BusinessRatingSummary.builder()
                .businessId(businessId)
                .averageRating(averageRating != null ? averageRating : 0.0)
                .totalReviews(totalReviews != null ? totalReviews : 0L)
                .fiveStarCount(ratingCounts.get(5))
                .fourStarCount(ratingCounts.get(4))
                .threeStarCount(ratingCounts.get(3))
                .twoStarCount(ratingCounts.get(2))
                .oneStarCount(ratingCounts.get(1))
                .build();
    }

    private ReviewResponseDTO mapToDTO(Review review) {
        ReviewResponseDTO dto = ReviewResponseDTO.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .businessId(review.getBusinessId())
                .serviceId(review.getServiceId())
                .appointmentId(review.getAppointmentId())
                .rating(review.getRating())
                .title(review.getTitle())
                .comment(review.getComment())
                .isVerifiedPurchase(review.getIsVerifiedPurchase())
                .isPublished(review.getIsPublished())
                .helpfulCount(review.getHelpfulCount())
                .notHelpfulCount(review.getNotHelpfulCount())
                .createdAt(review.getCreatedAt())
                .build();

        // Get staff response if exists
        responseRepository.findByReviewId(review.getId())
                .ifPresent(response -> dto.setStaffResponse(response.getResponseMessage()));

        return dto;
    }
}
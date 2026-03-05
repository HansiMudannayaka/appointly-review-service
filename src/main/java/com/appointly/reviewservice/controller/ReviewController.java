package com.appointly.reviewservice.controller;

import com.appointly.reviewservice.dto.request.CreateReviewRequest;
import com.appointly.reviewservice.dto.response.BusinessRatingSummary;
import com.appointly.reviewservice.dto.response.ReviewResponseDTO;
import com.appointly.reviewservice.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Management", description = "Endpoints for managing reviews and ratings")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Create a new review", description = "Creates a new review for a business or service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Review already exists for this appointment")
    })
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody CreateReviewRequest request) {
        ReviewResponseDTO createdReview = reviewService.createReview(request);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/business/{businessId}")
    @Operation(summary = "Get reviews by business ID", description = "Returns paginated list of reviews for a specific business")
    public ResponseEntity<Page<ReviewResponseDTO>> getReviewsByBusiness(
            @Parameter(description = "Business ID") @PathVariable Long businessId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ReviewResponseDTO> reviews = reviewService.getReviewsByBusinessId(businessId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get reviews by user ID", description = "Returns paginated list of reviews written by a specific user")
    public ResponseEntity<Page<ReviewResponseDTO>> getReviewsByUser(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ReviewResponseDTO> reviews = reviewService.getReviewsByUserId(userId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get review by ID", description = "Returns a specific review by its ID")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long id) {
        ReviewResponseDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update review", description = "Updates an existing review")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody CreateReviewRequest request) {
        ReviewResponseDTO updatedReview = reviewService.updateReview(id, request);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete review", description = "Deletes a review by its ID")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/helpful")
    @Operation(summary = "Mark review as helpful", description = "Increments the helpful count for a review")
    public ResponseEntity<ReviewResponseDTO> markHelpful(@PathVariable Long id) {
        ReviewResponseDTO review = reviewService.markHelpful(id);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/business/{businessId}/rating-summary")
    @Operation(summary = "Get business rating summary", description = "Returns rating statistics for a business")
    public ResponseEntity<BusinessRatingSummary> getBusinessRatingSummary(@PathVariable Long businessId) {
        BusinessRatingSummary summary = reviewService.getBusinessRatingSummary(businessId);
        return ResponseEntity.ok(summary);
    }
}
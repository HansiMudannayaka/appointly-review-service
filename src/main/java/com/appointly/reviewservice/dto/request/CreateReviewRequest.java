package com.appointly.reviewservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for creating a new review")
public class CreateReviewRequest {

    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the user writing the review", example = "123")
    private Long userId;

    @NotNull(message = "Business ID is required")
    @Schema(description = "ID of the business being reviewed", example = "456")
    private Long businessId;

    @Schema(description = "ID of the specific service (optional)", example = "789")
    private Long serviceId;

    @NotNull(message = "Appointment ID is required")
    @Schema(description = "ID of the appointment this review is for", example = "101112")
    private Long appointmentId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    @Schema(description = "Rating from 1 to 5 stars", example = "4")
    private Integer rating;

    @Size(max = 200, message = "Title must not exceed 200 characters")
    @Schema(description = "Review title", example = "Great service!")
    private String title;

    @Size(max = 1000, message = "Comment must not exceed 1000 characters")
    @Schema(description = "Detailed review comment", example = "The staff was very professional...")
    private String comment;
}
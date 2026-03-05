package com.appointly.reviewservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Review response object")
public class ReviewResponseDTO {

    @Schema(description = "Review ID")
    private Long id;

    @Schema(description = "User ID")
    private Long userId;

    @Schema(description = "Business ID")
    private Long businessId;

    @Schema(description = "Service ID")
    private Long serviceId;

    @Schema(description = "Appointment ID")
    private Long appointmentId;

    @Schema(description = "Rating (1-5)")
    private Integer rating;

    @Schema(description = "Review title")
    private String title;

    @Schema(description = "Review comment")
    private String comment;

    @Schema(description = "Whether the purchase is verified")
    private Boolean isVerifiedPurchase;

    @Schema(description = "Whether the review is published")
    private Boolean isPublished;

    @Schema(description = "Number of helpful votes")
    private Integer helpfulCount;

    @Schema(description = "Number of not helpful votes")
    private Integer notHelpfulCount;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Staff response if any")
    private String staffResponse;
}
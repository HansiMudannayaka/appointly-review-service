package com.appointly.reviewservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Business rating summary")
public class BusinessRatingSummary {

    @Schema(description = "Business ID")
    private Long businessId;

    @Schema(description = "Average rating")
    private Double averageRating;

    @Schema(description = "Total number of reviews")
    private Long totalReviews;

    @Schema(description = "Number of 5-star reviews")
    private Long fiveStarCount;

    @Schema(description = "Number of 4-star reviews")
    private Long fourStarCount;

    @Schema(description = "Number of 3-star reviews")
    private Long threeStarCount;

    @Schema(description = "Number of 2-star reviews")
    private Long twoStarCount;

    @Schema(description = "Number of 1-star reviews")
    private Long oneStarCount;
}
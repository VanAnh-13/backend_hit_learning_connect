package com.hit.leaning_connect.domain.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ClassEvaluationRequestDto(
    @NotNull(message = "Class ID is required")
    Long classId,
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    Integer rating,
    
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    String comment
) {
}

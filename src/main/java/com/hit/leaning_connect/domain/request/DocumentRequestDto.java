package com.hit.leaning_connect.domain.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record DocumentRequestDto(
    @NotNull(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    String title,
    
    String description,
    
    @NotNull(message = "File URL is required")
    String fileUrl
) {
}

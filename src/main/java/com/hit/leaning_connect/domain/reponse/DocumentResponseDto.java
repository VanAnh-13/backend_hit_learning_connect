package com.hit.leaning_connect.domain.reponse;

import java.time.LocalDateTime;

public record DocumentResponseDto(
    Long id,
    Long uploaderId,
    String uploaderUsername,
    String uploaderFullName,
    String title,
    String description,
    String fileUrl,
    LocalDateTime uploadedAt
) {
}

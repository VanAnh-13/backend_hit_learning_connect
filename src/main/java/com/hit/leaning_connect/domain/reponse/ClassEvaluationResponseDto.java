package com.hit.leaning_connect.domain.reponse;

import java.time.LocalDateTime;

public record ClassEvaluationResponseDto(
    Long id,
    Long classId,
    String classTitle,
    Long userId,
    String username,
    String userFullName,
    Integer rating,
    String comment,
    LocalDateTime createdAt
) {
}

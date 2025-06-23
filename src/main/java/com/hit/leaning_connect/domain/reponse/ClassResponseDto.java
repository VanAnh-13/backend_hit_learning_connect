package com.hit.leaning_connect.domain.reponse;

import java.time.LocalDate;

public record ClassResponseDto(
        Long id,
        String title,
        String description,
        Long teacherId,
        String teacherUsername,
        String teacherFullName,
        LocalDate startDate,
        LocalDate endDate
) {
}

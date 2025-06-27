package com.example.projectbase.domain.dto.response.classes;

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
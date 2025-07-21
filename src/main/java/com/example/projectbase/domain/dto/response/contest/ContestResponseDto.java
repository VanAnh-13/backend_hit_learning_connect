package com.example.projectbase.domain.dto.response.contest;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContestResponseDto {
    String title;
    String description;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String creator;
}

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

    private String contestId;

    private String title;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String fileUrl;

    private double highestScore;

    private String resultSummary;

    private String ranking;

    private String status;

}

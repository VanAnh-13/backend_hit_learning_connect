package com.example.projectbase.domain.dto.response.contest;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ContestUserResponseDto {

    private String contestId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double highestScore;
    private String resultSummary;
    private String ranking;

}

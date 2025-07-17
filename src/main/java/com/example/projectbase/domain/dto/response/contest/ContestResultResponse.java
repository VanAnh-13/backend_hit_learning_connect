package com.example.projectbase.domain.dto.response.contest;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ContestResultResponse {

    private String contestId;

    private String title;

    private String description;

    private String fileUrl;

    private double highestScore;

    private String resultSummary;

    private String ranking;
}

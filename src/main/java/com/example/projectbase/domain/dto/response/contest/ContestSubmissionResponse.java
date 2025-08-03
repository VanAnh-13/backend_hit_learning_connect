package com.example.projectbase.domain.dto.response.contest;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class ContestSubmissionResponse {

    private Long submissionId;
    private double highestScore;
    private String resultSummary;
    private String ranking;
    private String fileUrl;
    private Long contestId;
    private LocalDateTime submittedAt;
}

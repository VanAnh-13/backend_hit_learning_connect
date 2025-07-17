package com.example.projectbase.domain.dto.response.contest;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ContestReponseDto {

    private String contestId;

    private String title;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String fileUrl;

    private double highestScore;

    private String resultSummary;

    private String ranking;

}

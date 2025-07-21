package com.example.projectbase.domain.dto.request.contest;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ContestSubmissionDto {

    private String code;

    private double highestScore;

    private String resultSummary;

    private String ranking;

}

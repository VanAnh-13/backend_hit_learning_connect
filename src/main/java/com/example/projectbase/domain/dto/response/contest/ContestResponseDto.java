package com.example.projectbase.domain.dto.response.contest;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endTime;

    private String fileUrl;

    private double highestScore;

    private String resultSummary;

    private String ranking;

    private String status;

}

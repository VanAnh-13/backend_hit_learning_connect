package com.example.projectbase.domain.dto.request.contest;

import jakarta.persistence.Column;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;


import java.time.LocalDateTime;
//
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ContestUpdateDto {

    private String title;

    private String description;

//    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startTime;

//    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endTime;

    private String urlFile;

    private double highestScore;

    private String resultSummary;

    private String ranking;

    private String status;


}

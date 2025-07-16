package com.example.projectbase.domain.dto.request.contest;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ContestUpdateDto {

    private String title;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String fileUrl;

}

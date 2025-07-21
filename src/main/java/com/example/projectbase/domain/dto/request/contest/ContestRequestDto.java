package com.example.projectbase.domain.dto.request.contest;

import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContestRequestDto {
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

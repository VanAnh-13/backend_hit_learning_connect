package com.example.projectbase.domain.dto.request.contest;

import com.example.projectbase.constant.ErrorMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ContestCreatetDto {

    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    private String title;

    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    private String description;

    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    private LocalDateTime startTime;

    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    private LocalDateTime endTime;

    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    private String fileUrl;
}

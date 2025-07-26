package com.example.projectbase.domain.dto.request.contest;

import com.example.projectbase.constant.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    private String fileUrl;

//    @Schema(type = "string", format = "binary")
//    private MultipartFile file;

}

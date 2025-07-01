package com.example.projectbase.domain.dto.response.storage;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadFileResponseDto {
    private String fileName;
    private String fileType;
    private String filePath;
    private Long fileSize;
}

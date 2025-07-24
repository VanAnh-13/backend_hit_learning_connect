package com.example.projectbase.domain.dto.request.document;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequestDto {
    private String title;
    private String description;
    private String fileUrl;
    private Long classId;
}

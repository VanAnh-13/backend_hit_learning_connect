package com.example.projectbase.domain.dto.response.document;

import com.example.projectbase.domain.dto.response.user.UserResponseDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentResponseDto {
    private String id;
    private String title;
    private UserResponseDto creator;
    private String createdAt;
    private String fileUrl;
    private String description;
}

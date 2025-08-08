package com.example.projectbase.domain.dto.request.blog;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class BlogUpdateDto {
    private String title;
    private String description;
    private String tags;
    private String urlFile;
}

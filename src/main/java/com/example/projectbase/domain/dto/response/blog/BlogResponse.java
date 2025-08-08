package com.example.projectbase.domain.dto.response.blog;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class BlogResponse {

    private Long blogId;
    private String author;
    private String title;
    private String description;
    private String urlFile;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

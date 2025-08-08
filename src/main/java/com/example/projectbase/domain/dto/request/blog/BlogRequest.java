package com.example.projectbase.domain.dto.request.blog;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class BlogRequest {
    private String title;
    private String description;
    private String tags;
    private String urlFile;
}

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
    private String content;
    private List<Long> tagIds;
//    private MultipartFile image;

}

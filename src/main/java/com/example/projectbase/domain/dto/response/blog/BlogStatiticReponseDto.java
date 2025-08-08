package com.example.projectbase.domain.dto.response.blog;

import jakarta.persistence.SqlResultSetMapping;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BlogStatiticReponseDto {

    private Long blogId;
    private String title;
    private String author;
    private Long viewCount;
    private Long viewLike;
    private Long commentCount;

}

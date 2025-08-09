package com.example.projectbase.domain.dto.request.blog;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SearchBlogRequest {
    String keyword;
}

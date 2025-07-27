package com.example.projectbase.domain.dto.request.comment;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder


public class CommentRequest {

    private Long blogId;
    private String content;

}

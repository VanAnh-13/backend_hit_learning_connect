package com.example.projectbase.domain.dto.request.comment;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder


public class CommentRequest {
    private Long blogId;
    private String content;
    private LocalDateTime createdAt;
}

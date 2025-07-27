package com.example.projectbase.domain.dto.response.comment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CommentResponse {

    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
}

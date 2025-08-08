package com.example.projectbase.domain.dto.request.blog;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BlogApprovalRequestDto {
    private String status;
}

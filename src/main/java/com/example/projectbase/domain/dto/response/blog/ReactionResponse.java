package com.example.projectbase.domain.dto.response.blog;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class ReactionResponse {
    Long LIKE;
    Long SAD;
    Long HAHA;
    Long WOW;
    Long LOVE;
    Long ANGRY;
}

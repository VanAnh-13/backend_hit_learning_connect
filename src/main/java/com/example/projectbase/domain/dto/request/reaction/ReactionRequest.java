package com.example.projectbase.domain.dto.request.reaction;

import com.example.projectbase.domain.model.ReactionType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class ReactionRequest {
    private Long blogId;
    private ReactionType type;
}

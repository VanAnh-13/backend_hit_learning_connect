package com.example.projectbase.domain.dto.response.reaction;

import com.example.projectbase.domain.model.ReactionType;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class ReactionReponseDto {

    private Map<ReactionType, Long> counts;

}



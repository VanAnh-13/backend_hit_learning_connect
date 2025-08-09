package com.example.projectbase.domain.dto.response.classes;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalResponse {
    private Long totalClass;
    private Long totalAdmin;
    private Long totalUser;
}

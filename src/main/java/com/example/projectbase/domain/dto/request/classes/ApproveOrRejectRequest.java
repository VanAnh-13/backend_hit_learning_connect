package com.example.projectbase.domain.dto.request.classes;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ApproveOrRejectRequest {
    private Long registrationId;
    private boolean approved;
}

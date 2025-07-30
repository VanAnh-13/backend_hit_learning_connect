package com.example.projectbase.domain.dto.response.classes;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ClassRegistrationResponse {

    private Long registrationId;
    private String classTitle;
    private String studentEmail;
    private boolean pending;
    private LocalDateTime registeredAt;
    private String RegistrationStatus;
    private ClassResponseDto classRoom;

}

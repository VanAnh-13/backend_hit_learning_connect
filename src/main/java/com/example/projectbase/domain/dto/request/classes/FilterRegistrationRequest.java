package com.example.projectbase.domain.dto.request.classes;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FilterRegistrationRequest {
    private Long classId;
    private String studentEmail;
}

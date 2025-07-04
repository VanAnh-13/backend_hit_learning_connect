package com.example.projectbase.domain.dto.request.classes;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RegisterClassRequest {

    @NotNull(message = "Class ID must not be left blank")
    private Long classId;
}

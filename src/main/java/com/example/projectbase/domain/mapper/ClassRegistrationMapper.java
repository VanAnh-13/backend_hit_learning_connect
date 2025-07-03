package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.entity.ClassRegistration;
import com.example.projectbase.domain.dto.response.classes.ClassRegistrationResponse;

import java.time.LocalDateTime;

public interface ClassRegistrationMapper {
    public static ClassRegistrationResponse toReponse(ClassRegistration reg){
        return ClassRegistrationResponse.builder()
                .registrationId(reg.getRegistrationId())
                .classTitle(reg.getClassEntity().getTitle())
                .registeredAt(reg.getRegisteredAt())
                .pending(reg.isPending())
                . studentEmail(reg.getStudent().getEmail())
                .build();
    }
}


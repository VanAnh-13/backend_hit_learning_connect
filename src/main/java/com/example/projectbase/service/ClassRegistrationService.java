package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.classes.RegisterClassRequest;

public interface ClassRegistrationService {

    void register(Long userId, RegisterClassRequest request);

}

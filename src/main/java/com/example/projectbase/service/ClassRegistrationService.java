package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.classes.ApproveOrRejectRequest;
import com.example.projectbase.domain.dto.request.classes.FilterRegistrationRequest;
import com.example.projectbase.domain.dto.request.classes.RegisterClassRequest;
import com.example.projectbase.domain.dto.response.classes.ClassRegistrationResponse;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface ClassRegistrationService {

    void register(Long userId, RegisterClassRequest request) throws BadRequestException;
    void approveOrReject(Long adminId, ApproveOrRejectRequest request);
    void deleteRegistration(Long id);
    void cancelRegistration(Long userId, Long classId);
    List<ClassRegistrationResponse> getRegistrationsByUser(Long userId);
    List<ClassRegistrationResponse> getAllRegistrations();
    List<ClassRegistrationResponse> filterRegistrations(FilterRegistrationRequest request);
}

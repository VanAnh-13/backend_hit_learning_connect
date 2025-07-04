package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.classes.ApproveOrRejectRequest;
import com.example.projectbase.domain.dto.request.classes.FilterRegistrationRequest;
import com.example.projectbase.domain.dto.request.classes.RegisterClassRequest;
import com.example.projectbase.domain.dto.response.classes.ClassRegistrationResponse;
import com.example.projectbase.security.UserPrincipal;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClassRegistrationService {

    void register(Long userId, RegisterClassRequest request) throws BadRequestException;
    void approveOrReject(Long adminId, ApproveOrRejectRequest request);
    void deleteRegistration(Long id);
    void cancelRegistration(Long userId, Long classId);
    Page<ClassRegistrationResponse> getRegistrationsByUser(Long userId, Pageable pageable, UserPrincipal principal);
    Page<ClassRegistrationResponse> getAllRegistrations(Pageable pageable);
    Page<ClassRegistrationResponse> filterRegistrations(FilterRegistrationRequest request, Pageable pageable);
}

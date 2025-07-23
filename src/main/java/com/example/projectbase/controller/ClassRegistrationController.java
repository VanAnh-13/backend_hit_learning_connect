package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.ResponseMessage;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.classes.ApproveOrRejectRequest;
import com.example.projectbase.domain.dto.request.classes.FilterRegistrationRequest;
import com.example.projectbase.domain.dto.request.classes.RegisterClassRequest;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.ClassRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RestApiV1
public class ClassRegistrationController {

    private final ClassRegistrationService classRegistrationService;

    @Tag(name = "class-registration-MEMBER-controller")
    @Operation(summary = " Register class", description = "Member")
    @PostMapping(UrlConstant.ClassRegistration.CREATE_REGISTRATION)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> register(@RequestBody RegisterClassRequest request,
                                      @Parameter(hidden = true) @CurrentUser UserPrincipal user) throws BadRequestException {
        classRegistrationService.register(user.getId(), request);
        return VsResponseUtil.success(ResponseMessage.REGISTER_SUCCESS);
    }

    @Tag(name = "class-registration-ADMIN-controller")
    @Operation(summary = "Approve or deny the application", description = "Admin / Leader")
    @PostMapping(UrlConstant.ClassRegistration.APPROVE_REGISTRATION)
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public ResponseEntity<?> approverOrReject(@RequestBody ApproveOrRejectRequest request,
                                              @Parameter(hidden = true) @CurrentUser UserPrincipal admin) {
        classRegistrationService.approveOrReject(admin.getId(), request);
        return VsResponseUtil.success(ResponseMessage.APPROVE_REJECT_SUCCESS);
    }

    @Tag(name = "class-registration-MEMBER-controller")
    @Operation(summary = "View the registered classes", description = "Member")
    @GetMapping(UrlConstant.ClassRegistration.VIEW_REGISTRATION)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMyRegistrations(@Parameter(hidden = true) @CurrentUser UserPrincipal user,
                                                @ParameterObject Pageable pageable) {
        try {
            return VsResponseUtil.success(classRegistrationService.getRegistrationsByUser(user.getId(), pageable, user));
        } catch (Exception e) {
            return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.ClassRegistration.GET_MY_REGISTRATIONS_FAILED);
        }
    }

    @Tag(name = "class-registration-ADMIN-controller")
    @Operation(summary = "Get all registrations", description = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(UrlConstant.ClassRegistration.BASE)
    public ResponseEntity<?> getAllRegistrations(@ParameterObject Pageable pageable) {
        try {
            return VsResponseUtil.success(classRegistrationService.getAllRegistrations(pageable));
        } catch (Exception e) {
            return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.ClassRegistration.GET_ALL_REGISTRATIONS_FAILED);
        }
    }

    @Tag(name = "class-registration-ADMIN-controller")
    @Operation(summary = "Filter registrations by class ", description = "Admin")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(UrlConstant.ClassRegistration.FILTER_REGISTRATION)
    public ResponseEntity<?> filterRegistrations(@RequestBody FilterRegistrationRequest request, @ParameterObject Pageable pageable) {
        try {
            return VsResponseUtil.success(classRegistrationService.filterRegistrations(request, pageable));
        } catch (Exception e) {
            return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.ClassRegistration.FILTER_REGISTRATIONS_FAILED);
        }
    }

    @Tag(name = "class-registration-ADMIN-controller")
    @Operation(summary = "delete registration", description = "Admin / Leader")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    @DeleteMapping(UrlConstant.ClassRegistration.DEL_REGISTRATION)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        classRegistrationService.deleteRegistration(id);
        return VsResponseUtil.success(ResponseMessage.DELETE_SUCCESS);
    }

    @Operation(summary = "Cancel the class registration", description = "Member")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(UrlConstant.ClassRegistration.CANCEL_REGISTRATION)
    public ResponseEntity<?> cancel(@PathVariable Long classId, @Parameter(hidden = true) @CurrentUser UserPrincipal user) {
        classRegistrationService.cancelRegistration(user.getId(), classId);
        return VsResponseUtil.success(ResponseMessage.CANCEL_SUCCESS);
    }


    @Tag(name = "class-registration-MEMBER-controller")
    @PreAuthorize("hasRole('USER')")
    @PostMapping(UrlConstant.ClassRegistration.ACCEPTED_REGISTRATION)
    public ResponseEntity<?> accept(@Parameter(hidden = true) @CurrentUser UserPrincipal user, @ParameterObject Pageable pageable) {
        return VsResponseUtil.success(classRegistrationService.acceptedList(user, pageable));
    }

}

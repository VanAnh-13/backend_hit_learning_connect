package com.example.projectbase.controller;

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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Getter;
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
@RequestMapping(UrlConstant.ClassRegistration.CLASS_REGISTRATION)
@RequiredArgsConstructor
@SecurityRequirement(name="bearerAuth")
@Validated

public class ClassRegistrationController {

    private final ClassRegistrationService classRegistrationService;

    @Operation(summary=" Register class", description = "Member")
    @PostMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<?> register(@RequestBody RegisterClassRequest request,
                                      @Parameter(hidden = true) @CurrentUser UserPrincipal user) throws BadRequestException {
        classRegistrationService.register(user.getId(), request);
        return VsResponseUtil.success(ResponseMessage.REGISTER_SUCCESS);
    }

    @Operation(summary = "Approve or deny the application", description = "ADMIN/LEADER")
    @PostMapping("/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public ResponseEntity<?> approverOrReject(@RequestBody ApproveOrRejectRequest request,
                                              @Parameter(hidden = true) @CurrentUser UserPrincipal admin){
        classRegistrationService.approveOrReject(admin.getId(), request);
        return VsResponseUtil.success(ResponseMessage.APPROVE_REJECT_SUCCESS);
    }

    @Operation(summary = "View the registered classes", description = "MEMBER")
    @GetMapping("/user")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<?> getMyRegistrations(@Parameter(hidden = true) @CurrentUser UserPrincipal user,
                                                @ParameterObject Pageable pageable){
        try{
            return VsResponseUtil.success(classRegistrationService.getRegistrationsByUser(user.getId(), pageable, user));
        }catch (Exception e){
            return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.ClassRegistration.GET_MY_REGISTRATIONS_FAILED);
        }
    }

    @Operation(summary = "Get all registrations")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllRegistrations(@ParameterObject Pageable pageable){
        try{
            return VsResponseUtil.success(classRegistrationService.getAllRegistrations(pageable));
        }catch (Exception e){
            return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.ClassRegistration.GET_ALL_REGISTRATIONS_FAILED);
        }
    }

    @Operation(summary = "Filter registrations by class and email")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/filter")
    public ResponseEntity<?> filterRegistrations(@RequestBody FilterRegistrationRequest request, @ParameterObject Pageable pageable){
        try{
            return VsResponseUtil.success(classRegistrationService.filterRegistrations( request, pageable));
        }catch (Exception e){
            return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.ClassRegistration.FILTER_REGISTRATIONS_FAILED);
        }
   }

   @Operation(summary = "delete register", description = "ADMIN/LEADER")
   @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
   @DeleteMapping("/{id}")
   public ResponseEntity<?> delete(@PathVariable Long id){
        classRegistrationService.deleteRegistration(id);
        return VsResponseUtil.success(ResponseMessage.DELETE_SUCCESS);
   }

  @Operation(summary = "Cancel the class registration", description = "MEMBER")
    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping("/cancel/{classId}")
    public ResponseEntity<?> cancel(@PathVariable Long classId, @Parameter(hidden = true) @CurrentUser UserPrincipal user){
        classRegistrationService.cancelRegistration(user.getId(), classId);
        return VsResponseUtil.success(ResponseMessage.CANCEL_SUCCESS);
  }
}

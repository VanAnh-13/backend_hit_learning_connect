package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.pagination.PaginationFullRequestDto;
import com.example.projectbase.domain.dto.request.ChangePassFirstTimeRequest;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@RestApiV1
public class UserController {

    private final UserService userService;

    @Tag(name = "user-controller-admin")
    @Operation(summary = "API get user")
    @GetMapping(UrlConstant.User.GET_USER)
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        return VsResponseUtil.success(userService.getUserById(userId));
    }

    @Tags({@Tag(name = "user-controller-admin"), @Tag(name = "user-controller")})
    @Operation(summary = "API get current user login")
    @GetMapping(UrlConstant.User.GET_CURRENT_USER)
    public ResponseEntity<?> getCurrentUser(@Parameter(name = "principal", hidden = true)
                                            @CurrentUser UserPrincipal principal) {
        return VsResponseUtil.success(userService.getCurrentUser(principal));
    }

    @Tag(name = "user-controller-admin")
    @Operation(summary = "API get all customer")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(UrlConstant.User.GET_USERS)
    public ResponseEntity<?> getUsers(@ParameterObject @PageableDefault(page = 0, size = 100, sort = "timestamp", direction = Sort.Direction.ASC)
                                              Pageable pageable) {
        return VsResponseUtil.success(userService.getUsers(pageable));
    }

    @Tag(name = "user-controller")
    @Operation(summary = "API change password for first time login (only first time)")
    @PostMapping(UrlConstant.User.PASSWORD_CHANGE_FIRST_TIME)
    public ResponseEntity<?> changePass(@Parameter(name = "principal", hidden = true)
                                        @CurrentUser UserPrincipal principal,
                                        @Valid ChangePassFirstTimeRequest changePassFirstTimeRequest) {
        userService.changePassword(changePassFirstTimeRequest, principal);
        return VsResponseUtil.success("Successfully changed password");
    }

}

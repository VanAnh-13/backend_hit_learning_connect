package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.user.*;
import com.example.projectbase.exception.extended.InvalidException;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.MailService;
import com.example.projectbase.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RequiredArgsConstructor
@RestController
@RestApiV1
@Validated
public class UserController {

    private final UserService userService;

//    CRUD user
    @Tag(name = "admin-controller")
    @Operation(summary = "API get user by id", description = "Admin / Leader")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    @GetMapping(UrlConstant.User.GET_USER)
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        return VsResponseUtil.success(userService.getUserById(userId));
    }

    @Tag(name = "admin-controller")
    @Operation(summary = "API create user", description = "Admin / Leader")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    @PostMapping(UrlConstant.User.CREATE_USER)
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateDto user) throws InvalidException {
        return VsResponseUtil.success(userService.createUser(user));
    }

    @Tag(name = "admin-controller")
    @Operation(summary = "API update user by id", description = "Admin / Leader")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    @PutMapping(UrlConstant.User.UPDATE_USER)
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody @Valid UserUpdateDto user) {
        return VsResponseUtil.success(userService.updateUser(userId, user));
    }

    @Tag(name = "admin-controller")
    @Operation(summary = "API delete user by id", description = "Admin / Leader")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    @DeleteMapping(UrlConstant.User.DELETE_USER)
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return VsResponseUtil.success("User deleted");
    }

    @Tag(name = "admin-controller")
    @Operation(summary = "API get all customer", description = "Admin / Leader")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    @GetMapping(UrlConstant.User.GET_USERS)
    public ResponseEntity<?> getUsers(@ParameterObject @PageableDefault(page = 0, size = 100, sort = "timestamp", direction = Sort.Direction.ASC)
                                      Pageable pageable) {
        return VsResponseUtil.success(userService.getUsers(pageable));
    }




    @Tag(name = "user-controller")
    @Operation(summary = "API update current user's profile", description = "Authenticated")
    @PutMapping(UrlConstant.User.UPDATE_CURRENT_USER)
    public ResponseEntity<?> updateCurrentUser(@Parameter(name = "principal", hidden = true)
                                               @CurrentUser UserPrincipal principal,
                                               @RequestBody @Valid UserUpdateDto userUpdate) {
        userUpdate.setRole("");
        return VsResponseUtil.success(userService.updateUser(principal.getId(), userUpdate));
    }

    //  @Tags({@Tag(name = "admin-controller"), @Tag(name = "user-controller")})
    @Operation(summary = "API get current user login", description = "Authenticated")
    @GetMapping(UrlConstant.User.GET_CURRENT_USER)
    public ResponseEntity<?> getCurrentUser(@Parameter(name = "principal", hidden = true)
                                            @CurrentUser UserPrincipal principal) {
        return VsResponseUtil.success(userService.getCurrentUser(principal));
    }



}

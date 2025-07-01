package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.auth.LoginRequestDto;
import com.example.projectbase.domain.dto.request.auth.TokenRefreshRequestDto;
import com.example.projectbase.service.AuthService;
import com.example.projectbase.validator.annotation.ValidFileImage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Validated
@RestApiV1
@RestController
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "API Login")
  @PostMapping(UrlConstant.Auth.LOGIN)
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
    return VsResponseUtil.success(authService.login(request));
  }

//  @Operation(summary = "API test")
//  @PostMapping("auth/test")
//  public String login(@ValidFileImage MultipartFile multipartFile) {
//    return multipartFile.getContentType();
//  }
  @Operation(summary = "API Refresh Token")
  @PostMapping(UrlConstant.Auth.refreshToken)
  public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequestDto request) {
    return VsResponseUtil.success(authService.refresh(request));
  }
}

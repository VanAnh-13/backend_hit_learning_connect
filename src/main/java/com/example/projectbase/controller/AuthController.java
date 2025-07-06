package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.auth.LoginRequestDto;
import com.example.projectbase.domain.dto.request.auth.TokenRefreshRequestDto;
import com.example.projectbase.domain.dto.request.user.ChangePassFirstTimeRequest;
import com.example.projectbase.domain.dto.request.user.ChangePassRequest;
import com.example.projectbase.domain.dto.request.user.GetEmailDto;
import com.example.projectbase.domain.dto.request.user.VerifyCodeDto;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.AuthService;
import com.example.projectbase.service.MailService;
import com.example.projectbase.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Validated
@RestApiV1
@RestController
public class AuthController {

  private final AuthService authService;

  private final UserService userService;

  private final MailService mailService;

  @Operation(summary = "API Login", description = "Anonymous")
  @PostMapping(UrlConstant.Auth.LOGIN)
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
    return VsResponseUtil.success(authService.login(request));
  }

  @Operation(summary = "API get Access Token from Refresh Token", description = "Anonymous")
  @PostMapping(UrlConstant.Auth.refreshToken)
  public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequestDto request) {
    return VsResponseUtil.success(authService.refresh(request));
  }

  //change password
//  @Tag(name = "user-controller")
  @Operation(summary = "API change password for first time login (only first time)", description = "Authenticated")
  @PostMapping(UrlConstant.Auth.PASSWORD_CHANGE_FIRST_TIME)
  public ResponseEntity<?> changePass(@Parameter(name = "principal", hidden = true)
                                      @CurrentUser UserPrincipal principal,
                                      @Valid ChangePassFirstTimeRequest changePassFirstTimeRequest) {
    userService.changePasswordFirstTime(changePassFirstTimeRequest, principal);
    return VsResponseUtil.success("Successfully changed password");
  }

//  @Tag(name = "user-controller")
  @Operation(summary = "API request code to email, get code too much then ban ip for 20 minute", description = "permitAll")
  @PostMapping(UrlConstant.Auth.SEND_CODE)
  public ResponseEntity<?> sendCode(@Parameter(name = "email") @RequestBody GetEmailDto email) throws Exception {
    return VsResponseUtil.success(mailService.sendMail(email));
  }

//  @Tag(name = "user-controller")
  @Operation(summary = "API verify code to change password", description = "permitAll")
  @PostMapping(UrlConstant.Auth.VERIFY_CODE)
  public ResponseEntity<?> verifyCode(@RequestBody VerifyCodeDto verifyCodeDto) throws BadRequestException {
    return VsResponseUtil.success(mailService.verifyEmail(verifyCodeDto));
  }

//  @Tag(name = "user-controller")
  @Operation(summary = "API change password inside account", description = "Authenticated")
  @PostMapping(UrlConstant.Auth.PASSWORD_CHANGE)
  public ResponseEntity<?> changePassword(
          @Parameter(name = "principal", hidden = true)
          @CurrentUser UserPrincipal principal,
          @RequestBody @Valid ChangePassRequest changePassRequest
  ) {
    return VsResponseUtil.success(userService.changePassword(changePassRequest, principal));
  }


}

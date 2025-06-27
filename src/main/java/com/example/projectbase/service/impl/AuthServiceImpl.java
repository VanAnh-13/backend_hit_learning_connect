package com.example.projectbase.service.impl;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.request.LoginRequestDto;
import com.example.projectbase.domain.dto.request.TokenRefreshRequestDto;
import com.example.projectbase.domain.dto.response.CommonResponseDto;
import com.example.projectbase.domain.dto.response.LoginResponseDto;
import com.example.projectbase.domain.dto.response.TokenRefreshResponseDto;
import com.example.projectbase.exception.extended.UnauthorizedException;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.security.jwt.JwtTokenProvider;
import com.example.projectbase.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public LoginResponseDto login(LoginRequestDto request) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
      System.out.println(userPrincipal.getId());
      String accessToken = jwtTokenProvider.generateToken(userPrincipal, Boolean.FALSE);
      String refreshToken = jwtTokenProvider.generateToken(userPrincipal, Boolean.TRUE);
      return new LoginResponseDto(accessToken, refreshToken, userPrincipal.getId(), authentication.getAuthorities());
    } catch (InternalAuthenticationServiceException e) {
      throw new UnauthorizedException(ErrorMessage.Auth.ERR_INCORRECT_USERNAME);
    } catch (BadCredentialsException e) {
      throw new UnauthorizedException(ErrorMessage.Auth.ERR_INCORRECT_PASSWORD);
    }
  }

  @Override
  public TokenRefreshResponseDto refresh(TokenRefreshRequestDto request) {
    Authentication authentication = jwtTokenProvider.getAuthenticationByRefreshToken(request.getRefreshToken());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    System.out.println(userPrincipal.getId());
    String accessToken = jwtTokenProvider.generateToken(userPrincipal, Boolean.FALSE);
    System.out.println(userPrincipal.getId() + "hello");
    return new TokenRefreshResponseDto(accessToken, request.getRefreshToken());
  }

  @Override
  public CommonResponseDto logout(HttpServletRequest request) {
    return null;
  }

}

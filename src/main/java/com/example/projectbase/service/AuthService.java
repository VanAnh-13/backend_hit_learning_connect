package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.auth.LoginRequestDto;
import com.example.projectbase.domain.dto.request.auth.TokenRefreshRequestDto;
import com.example.projectbase.domain.dto.response.CommonResponseDto;
import com.example.projectbase.domain.dto.response.auth.LoginResponseDto;
import com.example.projectbase.domain.dto.response.auth.TokenRefreshResponseDto;

import com.example.projectbase.domain.dto.response.classes.TotalResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

  LoginResponseDto login(LoginRequestDto request);

  TokenRefreshResponseDto refresh(TokenRefreshRequestDto request);

  CommonResponseDto logout(HttpServletRequest request);

  TotalResponse getTotal();
}

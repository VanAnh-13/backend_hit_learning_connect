package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.user.ChangePassFirstTimeRequest;
import com.example.projectbase.domain.dto.request.user.ChangePassRequest;
import com.example.projectbase.domain.dto.request.user.UserCreateDto;
import com.example.projectbase.domain.dto.request.user.UserUpdateDto;
import com.example.projectbase.domain.dto.response.user.UserResponseDto;
import com.example.projectbase.exception.extended.InvalidException;
import com.example.projectbase.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

  UserResponseDto getUserById(Long userId);

  List<UserResponseDto> getUsers(Pageable pageable);

  UserResponseDto getCurrentUser(UserPrincipal principal);

  void changePasswordFirstTime(@Valid ChangePassFirstTimeRequest changePassFirstTimeRequest, UserPrincipal principal);

  UserCreateDto createUser(UserCreateDto user) throws InvalidException;

  UserResponseDto updateUser(Long id, UserUpdateDto updatedUser);

  void deleteUser(Long id);

  String changePassword(ChangePassRequest changePassRequest, UserPrincipal userPrincipal);

  Boolean checkFirstLogin(UserPrincipal userPrincipal);
}

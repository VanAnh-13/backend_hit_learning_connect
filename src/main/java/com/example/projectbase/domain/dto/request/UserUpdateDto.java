package com.example.projectbase.domain.dto.request;

import com.example.projectbase.constant.ErrorMessage;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDto {

  private String username;

  private String fullname;

  private String email;

  private String Role;

  private String avatarUrl;

}

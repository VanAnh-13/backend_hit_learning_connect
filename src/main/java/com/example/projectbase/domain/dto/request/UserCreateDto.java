package com.example.projectbase.domain.dto.request;

import com.example.projectbase.constant.ErrorMessage;

import com.example.projectbase.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCreateDto {

  @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
  private String username;

  @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$", message = ErrorMessage.INVALID_FORMAT_PASSWORD)
  private String password;

  @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
  private String fullName;

  @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
  private String email;

  @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
  private String Role;

}

package com.example.projectbase.domain.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

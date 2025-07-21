package com.example.projectbase.domain.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDto {

  private String username;

  private String fullname;

  private String email;

  private String Role;

  private String gender;

  private String birthday;

  private String avatarUrl;

//  private String gender;
//
//  private LocalDate birthday;

}

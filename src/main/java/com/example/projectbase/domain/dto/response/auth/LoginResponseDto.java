package com.example.projectbase.domain.dto.response.auth;

import com.example.projectbase.constant.CommonConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@NoArgsConstructor
@Setter
@Getter
public class LoginResponseDto {

  private String tokenType = CommonConstant.BEARER_TOKEN;

  private String accessToken;

  private String refreshToken;

  private Long id;

  private boolean firstLogin;

  private Collection<? extends GrantedAuthority> authorities;

  public LoginResponseDto(String accessToken, String refreshToken, Long id, boolean firstLogin, Collection<? extends GrantedAuthority> authorities) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.id = id;
    this.firstLogin = firstLogin;
    this.authorities = authorities;
  }

}

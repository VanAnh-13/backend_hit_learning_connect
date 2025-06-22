package com.example.base.product.domain.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UserUpdateRequest {

    private String username;

    private String birthday;
    private String password;

    private String address;
    private String phoneNumber;

    private String gender;

    private String email;

    private String fullName;;
    private String role;
}

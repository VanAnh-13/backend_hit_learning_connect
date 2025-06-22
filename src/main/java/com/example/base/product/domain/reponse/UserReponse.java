package com.example.base.product.domain.reponse;

import com.example.base.product.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserReponse {

    private Long id;
    private String username;
    private Set<Role> roles;

}

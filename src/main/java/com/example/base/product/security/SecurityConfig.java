package com.example.base.product.security;

import com.example.base.product.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

}

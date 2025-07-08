package com.hit.leaning_connect.security;

import com.hit.leaning_connect.constant.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectURL = request.getContextPath();
        boolean isAdmin = false;
        boolean isTeacher = false;
        boolean isUser = false;

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(Role.ADMIN.name())) {
                isAdmin = true;
            } else if (authority.getAuthority().equals(Role.LEADER.name())) {
                isTeacher = true;
            } else if (authority.getAuthority().equals(Role.MEMBER.name())) {
                isUser = true;
            }
        }

        if (isAdmin) {
            redirectURL = "/admin";
        } else if (isTeacher) {
            redirectURL = "/leader";
        } else if (isUser) {
            redirectURL = "/member";
        } else {
            redirectURL = "/login?error";
        }

        response.sendRedirect(redirectURL);
    }
}

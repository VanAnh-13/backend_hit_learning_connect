package com.hit.leaning_connect.domain.request;

public record AuthRequest(
        String username,
        String password
) {
}

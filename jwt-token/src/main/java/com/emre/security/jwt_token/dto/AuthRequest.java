package com.emre.security.jwt_token.dto;

public record AuthRequest(
        String username,
        String password
) {
}

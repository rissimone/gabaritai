package com.gabaritai.auth.web.dto;

public record LoginResponse(String accessToken, String tokenType, long expiresIn) {
}

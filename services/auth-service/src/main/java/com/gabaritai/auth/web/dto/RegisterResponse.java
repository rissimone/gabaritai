package com.gabaritai.auth.web.dto;

import java.util.UUID;

public record RegisterResponse(UUID id, String name, String email, String status) {
}

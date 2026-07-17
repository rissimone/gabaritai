package com.gabaritai.auth.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ConfirmEmailRequest(@NotBlank String token) {
}

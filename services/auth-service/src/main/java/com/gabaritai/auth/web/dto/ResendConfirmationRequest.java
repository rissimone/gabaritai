package com.gabaritai.auth.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResendConfirmationRequest(@NotBlank @Email String email) {
}

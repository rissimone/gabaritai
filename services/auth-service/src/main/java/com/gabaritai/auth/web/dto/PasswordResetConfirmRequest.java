package com.gabaritai.auth.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordResetConfirmRequest(
        @NotBlank String token,
        @NotBlank
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "A senha deve ter pelo menos 8 caracteres, incluindo letras e numeros.")
        String newPassword) {
}

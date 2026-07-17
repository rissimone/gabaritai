package com.gabaritai.auth.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(max = 150) String name,
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "A senha deve ter pelo menos 8 caracteres, incluindo letras e numeros.")
        String password,
        boolean termsAccepted) {
}

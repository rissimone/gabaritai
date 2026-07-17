package com.gabaritai.auth.web;

import com.gabaritai.auth.domain.User;
import com.gabaritai.auth.service.AuthenticationService;
import com.gabaritai.auth.service.EmailConfirmationService;
import com.gabaritai.auth.service.JwtService;
import com.gabaritai.auth.service.PasswordResetService;
import com.gabaritai.auth.service.RegistrationService;
import com.gabaritai.auth.web.dto.ConfirmEmailRequest;
import com.gabaritai.auth.web.dto.LoginRequest;
import com.gabaritai.auth.web.dto.LoginResponse;
import com.gabaritai.auth.web.dto.PasswordResetConfirmRequest;
import com.gabaritai.auth.web.dto.PasswordResetRequest;
import com.gabaritai.auth.web.dto.RegisterRequest;
import com.gabaritai.auth.web.dto.RegisterResponse;
import com.gabaritai.auth.web.dto.ResendConfirmationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** UC-001, UC-002, UC-003 (RF01). */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticacao", description = "Cadastro, login e recuperacao de senha")
public class AuthController {

    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final EmailConfirmationService emailConfirmationService;
    private final PasswordResetService passwordResetService;
    private final JwtService jwtService;

    public AuthController(
            RegistrationService registrationService,
            AuthenticationService authenticationService,
            EmailConfirmationService emailConfirmationService,
            PasswordResetService passwordResetService,
            JwtService jwtService) {
        this.registrationService = registrationService;
        this.authenticationService = authenticationService;
        this.emailConfirmationService = emailConfirmationService;
        this.passwordResetService = passwordResetService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Cadastrar novo usuario (UC-001)")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        User user = registrationService.register(
                request.name(), request.email(), request.password(), request.termsAccepted());
        return new RegisterResponse(user.getId(), user.getName(), user.getEmail(), user.getStatus().name());
    }

    @Operation(summary = "Confirmar e-mail cadastrado (UC-001)")
    @PostMapping("/email-confirmations")
    public ResponseEntity<Void> confirmEmail(@Valid @RequestBody ConfirmEmailRequest request) {
        emailConfirmationService.confirmEmail(request.token());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reenviar e-mail de confirmacao (UC-001, fluxo alternativo)")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/email-confirmations/resend")
    public void resendConfirmation(@Valid @RequestBody ResendConfirmationRequest request) {
        emailConfirmationService.resendConfirmation(request.email());
    }

    @Operation(summary = "Autenticar usuario (UC-002)")
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        String accessToken = authenticationService.login(request.email(), request.password());
        return new LoginResponse(accessToken, "Bearer", jwtService.getExpirationSeconds());
    }

    @Operation(summary = "Solicitar recuperacao de senha (UC-003)")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/password-resets")
    public void requestPasswordReset(@Valid @RequestBody PasswordResetRequest request) {
        passwordResetService.requestReset(request.email());
    }

    @Operation(summary = "Confirmar nova senha com token de recuperacao (UC-003)")
    @PutMapping("/password-resets")
    public ResponseEntity<Void> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmRequest request) {
        passwordResetService.confirmReset(request.token(), request.newPassword());
        return ResponseEntity.ok().build();
    }
}

package com.gabaritai.user.web;

import com.gabaritai.user.domain.UserProfile;
import com.gabaritai.user.exception.MissingUserIdentityException;
import com.gabaritai.user.service.ProfileService;
import com.gabaritai.user.web.dto.ProfileResponse;
import com.gabaritai.user.web.dto.UpdateProfileRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UC-004 — Editar perfil (RF02). A identidade do candidato vem do header X-User-Id, que o
 * API Gateway garante estar presente e valido apos autenticar o JWT (RNF-009) — este servico
 * nao fica exposto publicamente (RNF-018/019), entao confia nesse header.
 */
@RestController
@RequestMapping("/api/v1/usuarios/perfil")
@Tag(name = "Perfil", description = "Perfil e preferencias do candidato")
public class ProfileController {

    private static final String USER_ID_HEADER = "X-User-Id";

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Operation(summary = "Consultar o perfil do candidato autenticado (UC-004)")
    @GetMapping
    public ProfileResponse getProfile(
            @Parameter(hidden = true) @RequestHeader(value = USER_ID_HEADER, required = false) String userIdHeader) {
        UserProfile profile = profileService.getOrCreateProfile(parseUserId(userIdHeader));
        return ProfileResponse.from(profile);
    }

    @Operation(summary = "Atualizar o perfil do candidato autenticado (UC-004)")
    @PutMapping
    public ProfileResponse updateProfile(
            @Parameter(hidden = true) @RequestHeader(value = USER_ID_HEADER, required = false) String userIdHeader,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserProfile profile = profileService.updateProfile(
                parseUserId(userIdHeader),
                request.photoUrl(),
                request.phone(),
                request.timezone(),
                request.receiveNotifications(),
                request.experienceLevel(),
                request.studyGoal(),
                request.learningPreferences());
        return ProfileResponse.from(profile);
    }

    private UUID parseUserId(String userIdHeader) {
        try {
            return UUID.fromString(userIdHeader);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new MissingUserIdentityException();
        }
    }
}

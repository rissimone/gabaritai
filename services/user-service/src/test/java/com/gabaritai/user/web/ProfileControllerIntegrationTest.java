package com.gabaritai.user.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.gabaritai.user.AbstractIntegrationTest;
import com.gabaritai.user.domain.ExperienceLevel;
import com.gabaritai.user.domain.LearningPreference;
import com.gabaritai.user.web.dto.ProfileResponse;
import com.gabaritai.user.web.dto.UpdateProfileRequest;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpEntity<Void> withUserId(UUID userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", userId.toString());
        return new HttpEntity<>(headers);
    }

    private HttpEntity<UpdateProfileRequest> withUserId(UUID userId, UpdateProfileRequest body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", userId.toString());
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    @Test
    void getProfile_withoutUserIdHeader_returnsUnauthorized() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/usuarios/perfil", HttpMethod.GET, HttpEntity.EMPTY, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void getProfile_forNewUser_createsDefaultProfile() {
        UUID userId = UUID.randomUUID();

        ResponseEntity<ProfileResponse> response = restTemplate.exchange(
                "/api/v1/usuarios/perfil", HttpMethod.GET, withUserId(userId), ProfileResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().userId()).isEqualTo(userId);
        assertThat(response.getBody().timezone()).isEqualTo("America/Sao_Paulo");
    }

    @Test
    void updateProfile_withValidData_persistsChanges() {
        UUID userId = UUID.randomUUID();
        UpdateProfileRequest request = new UpdateProfileRequest(
                "https://example.com/foto.jpg",
                "+55 11 98888-0000",
                "America/Bahia",
                false,
                ExperienceLevel.EXPERIENTE,
                "Passar no concurso",
                Set.of(LearningPreference.VIDEO, LearningPreference.REVISAO));

        ResponseEntity<ProfileResponse> response = restTemplate.exchange(
                "/api/v1/usuarios/perfil", HttpMethod.PUT, withUserId(userId, request), ProfileResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().photoUrl()).isEqualTo("https://example.com/foto.jpg");
        assertThat(response.getBody().timezone()).isEqualTo("America/Bahia");
        assertThat(response.getBody().experienceLevel()).isEqualTo(ExperienceLevel.EXPERIENTE);
        assertThat(response.getBody().learningPreferences())
                .containsExactlyInAnyOrder(LearningPreference.VIDEO, LearningPreference.REVISAO);

        // GET subsequente deve refletir a atualizacao (nao um novo perfil default).
        ResponseEntity<ProfileResponse> getResponse = restTemplate.exchange(
                "/api/v1/usuarios/perfil", HttpMethod.GET, withUserId(userId), ProfileResponse.class);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().timezone()).isEqualTo("America/Bahia");
    }

    @Test
    void updateProfile_withInvalidTimezone_returnsBadRequest() {
        UUID userId = UUID.randomUUID();
        UpdateProfileRequest request = new UpdateProfileRequest(
                null, null, "Nao/Existe", true, null, null, Set.of());

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/usuarios/perfil", HttpMethod.PUT, withUserId(userId, request), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateProfile_withBlankTimezone_returnsBadRequestFromBeanValidation() {
        UUID userId = UUID.randomUUID();
        UpdateProfileRequest request = new UpdateProfileRequest(null, null, "", true, null, null, Set.of());

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/usuarios/perfil", HttpMethod.PUT, withUserId(userId, request), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

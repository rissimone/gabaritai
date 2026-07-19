package com.gabaritai.edital.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.gabaritai.edital.AbstractIntegrationTest;
import com.gabaritai.edital.domain.ConcursoStatus;
import com.gabaritai.edital.domain.EducationLevel;
import com.gabaritai.edital.web.dto.ConcursoResponse;
import com.gabaritai.edital.web.dto.CriarConcursoRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConcursoControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private <T> HttpEntity<T> withUserId(UUID userId, T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", userId.toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    private HttpEntity<Void> withUserId(UUID userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", userId.toString());
        return new HttpEntity<>(headers);
    }

    private CriarConcursoRequest requestPadrao(String orgao) {
        return new CriarConcursoRequest(
                orgao,
                "Banca Y",
                "Analista",
                "001/2026",
                LocalDate.of(2026, 12, 1),
                EducationLevel.SUPERIOR,
                List.of("Direito Constitucional", "Portugues"),
                50,
                "https://example.com/edital",
                null);
    }

    @Test
    void criarConcurso_semUserIdHeader_retornaUnauthorized() {
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/v1/concursos", new HttpEntity<>(requestPadrao("Tribunal X")), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void criarConcurso_comDadosValidos_retornaCreatedEDisciplinas() {
        UUID userId = UUID.randomUUID();

        ResponseEntity<ConcursoResponse> response = restTemplate.exchange(
                "/api/v1/concursos", HttpMethod.POST, withUserId(userId, requestPadrao("Tribunal X")),
                ConcursoResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(ConcursoStatus.PREVISTO);
        assertThat(response.getBody().disciplinas())
                .containsExactlyInAnyOrder("Direito Constitucional", "Portugues");
    }

    @Test
    void criarConcurso_duplicado_retornaConflict() {
        UUID userId = UUID.randomUUID();
        restTemplate.exchange(
                "/api/v1/concursos", HttpMethod.POST, withUserId(userId, requestPadrao("Tribunal Duplicado")),
                ConcursoResponse.class);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/concursos", HttpMethod.POST, withUserId(userId, requestPadrao("Tribunal Duplicado")),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void listarConcursos_retornaApenasOsDoUsuarioAutenticado() {
        UUID userA = UUID.randomUUID();
        UUID userB = UUID.randomUUID();
        restTemplate.exchange(
                "/api/v1/concursos", HttpMethod.POST, withUserId(userA, requestPadrao("Concurso A")),
                ConcursoResponse.class);
        restTemplate.exchange(
                "/api/v1/concursos", HttpMethod.POST, withUserId(userB, requestPadrao("Concurso B")),
                ConcursoResponse.class);

        ResponseEntity<ConcursoResponse[]> response = restTemplate.exchange(
                "/api/v1/concursos", HttpMethod.GET, withUserId(userA), ConcursoResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).extracting(ConcursoResponse::orgao).containsExactly("Concurso A");
    }

    @Test
    void buscarConcurso_deOutroUsuario_retornaNotFound() {
        UUID dono = UUID.randomUUID();
        UUID outro = UUID.randomUUID();
        ResponseEntity<ConcursoResponse> criado = restTemplate.exchange(
                "/api/v1/concursos", HttpMethod.POST, withUserId(dono, requestPadrao("Concurso Privado")),
                ConcursoResponse.class);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/concursos/" + criado.getBody().id(), HttpMethod.GET, withUserId(outro), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void criarConcurso_semCamposObrigatorios_retornaBadRequest() {
        UUID userId = UUID.randomUUID();
        CriarConcursoRequest request = new CriarConcursoRequest(
                "", "", "", null, null, null, List.of(), null, null, null);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/concursos", HttpMethod.POST, withUserId(userId, request), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

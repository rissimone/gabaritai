package com.gabaritai.auth.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.gabaritai.auth.AbstractIntegrationTest;
import com.gabaritai.auth.config.KafkaConfig;
import com.gabaritai.auth.messaging.NotificationEvent;
import com.gabaritai.auth.web.dto.ConfirmEmailRequest;
import com.gabaritai.auth.web.dto.LoginRequest;
import com.gabaritai.auth.web.dto.LoginResponse;
import com.gabaritai.auth.web.dto.PasswordResetConfirmRequest;
import com.gabaritai.auth.web.dto.PasswordResetRequest;
import com.gabaritai.auth.web.dto.RegisterRequest;
import com.gabaritai.auth.web.dto.RegisterResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.serializer.JsonDeserializer;

/**
 * Percorre o fluxo completo de UC-001/UC-002/UC-003 via HTTP, consumindo os eventos
 * assincronos publicados no Kafka (ver ADR-0002/ADR-0003) para obter os tokens de confirmacao/redefinicao.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthFlowIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private KafkaConsumer<String, NotificationEvent> consumer;

    @BeforeEach
    void setUpConsumer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "auth-flow-test-" + UUID.randomUUID());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, NotificationEvent.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.gabaritai.auth.messaging");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of(KafkaConfig.NOTIFICATIONS_TOPIC));
    }

    @AfterEach
    void tearDownConsumer() {
        consumer.close();
    }

    /**
     * Um unico topico compartilhado acumula eventos de todos os metodos de teste (e de execucoes
     * anteriores, ja que cada consumidor comeca do inicio do topico); por isso filtra pelo
     * e-mail esperado em vez de assumir que o proximo registro pertence a este teste.
     */
    private NotificationEvent awaitNotificationEvent(String expectedEmail) {
        long deadline = System.currentTimeMillis() + Duration.ofSeconds(15).toMillis();
        while (System.currentTimeMillis() < deadline) {
            ConsumerRecords<String, NotificationEvent> records = consumer.poll(Duration.ofMillis(500));
            for (ConsumerRecord<String, NotificationEvent> record : records) {
                if (expectedEmail.equals(record.value().recipientEmail())) {
                    return record.value();
                }
            }
        }
        throw new AssertionError("Evento de notificacao para " + expectedEmail + " nao chegou em 15s");
    }

    @Test
    void fullRegistrationLoginAndPasswordResetFlow() {
        String email = "candidato-" + System.nanoTime() + "@example.com";

        // UC-001: cadastro
        ResponseEntity<RegisterResponse> registerResponse = restTemplate.postForEntity(
                "/api/v1/auth/register",
                new RegisterRequest("Ana Candidata", email, "SenhaForte1", true),
                RegisterResponse.class);
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(registerResponse.getBody()).isNotNull();
        assertThat(registerResponse.getBody().status()).isEqualTo("PENDING_CONFIRMATION");

        // Login antes da confirmacao deve ser rejeitado (RF01).
        ResponseEntity<String> loginBeforeConfirm = restTemplate.postForEntity(
                "/api/v1/auth/login", new LoginRequest(email, "SenhaForte1"), String.class);
        assertThat(loginBeforeConfirm.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        // Consome o evento assincrono para obter o token de confirmacao (ADR-0002).
        NotificationEvent confirmationEvent = awaitNotificationEvent(email);
        assertThat(confirmationEvent.eventType()).isEqualTo("EMAIL_CONFIRMATION_REQUESTED");
        assertThat(confirmationEvent.recipientEmail()).isEqualTo(email);

        // UC-001: confirmacao de e-mail
        ResponseEntity<Void> confirmResponse = restTemplate.postForEntity(
                "/api/v1/auth/email-confirmations",
                new ConfirmEmailRequest(confirmationEvent.token()),
                Void.class);
        assertThat(confirmResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // UC-002: login com sucesso apos confirmacao
        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity(
                "/api/v1/auth/login", new LoginRequest(email, "SenhaForte1"), LoginResponse.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        assertThat(loginResponse.getBody().accessToken()).isNotBlank();

        // UC-003: solicitar redefinicao de senha
        ResponseEntity<Void> resetRequestResponse = restTemplate.postForEntity(
                "/api/v1/auth/password-resets", new PasswordResetRequest(email), Void.class);
        assertThat(resetRequestResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        NotificationEvent resetEvent = awaitNotificationEvent(email);
        assertThat(resetEvent.eventType()).isEqualTo("PASSWORD_RESET_REQUESTED");

        // UC-003: confirmar nova senha
        ResponseEntity<Void> resetConfirmResponse = restTemplate.exchange(
                "/api/v1/auth/password-resets",
                HttpMethod.PUT,
                new HttpEntity<>(new PasswordResetConfirmRequest(resetEvent.token(), "NovaSenhaForte2")),
                Void.class);
        assertThat(resetConfirmResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Login com a senha antiga deve falhar; com a nova, deve funcionar.
        ResponseEntity<String> loginWithOldPassword = restTemplate.postForEntity(
                "/api/v1/auth/login", new LoginRequest(email, "SenhaForte1"), String.class);
        assertThat(loginWithOldPassword.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        ResponseEntity<LoginResponse> loginWithNewPassword = restTemplate.postForEntity(
                "/api/v1/auth/login", new LoginRequest(email, "NovaSenhaForte2"), LoginResponse.class);
        assertThat(loginWithNewPassword.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void register_withDuplicateEmail_returnsConflict() {
        String email = "duplicado-" + System.nanoTime() + "@example.com";
        RegisterRequest request = new RegisterRequest("Ana", email, "SenhaForte1", true);

        restTemplate.postForEntity("/api/v1/auth/register", request, RegisterResponse.class);
        awaitNotificationEvent(email);

        ResponseEntity<String> secondAttempt = restTemplate.postForEntity(
                "/api/v1/auth/register", request, String.class);

        assertThat(secondAttempt.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void register_withoutAcceptingTerms_returnsBadRequest() {
        String email = "sem-termos-" + System.nanoTime() + "@example.com";

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/v1/auth/register",
                new RegisterRequest("Ana", email, "SenhaForte1", false),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void login_afterTooManyFailedAttempts_locksAccount() {
        String email = "bloqueio-" + System.nanoTime() + "@example.com";
        restTemplate.postForEntity(
                "/api/v1/auth/register",
                new RegisterRequest("Ana", email, "SenhaForte1", true),
                RegisterResponse.class);
        NotificationEvent confirmationEvent = awaitNotificationEvent(email);
        restTemplate.postForEntity(
                "/api/v1/auth/email-confirmations", new ConfirmEmailRequest(confirmationEvent.token()), Void.class);

        // application.yml de teste define max-failed-attempts=3.
        for (int i = 0; i < 3; i++) {
            restTemplate.postForEntity(
                    "/api/v1/auth/login", new LoginRequest(email, "SenhaErrada"), String.class);
        }

        ResponseEntity<String> lockedAttempt = restTemplate.postForEntity(
                "/api/v1/auth/login", new LoginRequest(email, "SenhaForte1"), String.class);

        assertThat(lockedAttempt.getStatusCode()).isEqualTo(HttpStatus.LOCKED);
    }
}

package com.gabaritai.auth.messaging;

import com.gabaritai.auth.config.KafkaConfig;
import com.gabaritai.auth.domain.User;
import java.time.Instant;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventPublisher {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public NotificationEventPublisher(KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishEmailConfirmationRequested(User user, String rawToken, Instant expiresAt) {
        NotificationEvent event = new NotificationEvent(
                "EMAIL_CONFIRMATION_REQUESTED", user.getEmail(), user.getName(), rawToken, expiresAt, Instant.now());
        // Chave = e-mail do destinatario, garantindo ordem por usuario dentro da particao (RNF-013).
        kafkaTemplate.send(KafkaConfig.NOTIFICATIONS_TOPIC, user.getEmail(), event);
    }

    public void publishPasswordResetRequested(User user, String rawToken, Instant expiresAt) {
        NotificationEvent event = new NotificationEvent(
                "PASSWORD_RESET_REQUESTED", user.getEmail(), user.getName(), rawToken, expiresAt, Instant.now());
        kafkaTemplate.send(KafkaConfig.NOTIFICATIONS_TOPIC, user.getEmail(), event);
    }
}

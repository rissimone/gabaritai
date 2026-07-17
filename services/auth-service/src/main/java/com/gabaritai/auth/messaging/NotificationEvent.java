package com.gabaritai.auth.messaging;

import java.time.Instant;

/**
 * Contrato de evento publicado na exchange {@code gabaritai.notifications} (ver ADR-0002).
 * O token trafega apenas neste evento assincrono; o banco do auth-service armazena somente o hash.
 */
public record NotificationEvent(
        String eventType,
        String recipientEmail,
        String recipientName,
        String token,
        Instant expiresAt,
        Instant occurredAt) {
}

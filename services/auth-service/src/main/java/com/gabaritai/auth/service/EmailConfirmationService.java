package com.gabaritai.auth.service;

import com.gabaritai.auth.domain.EmailConfirmationToken;
import com.gabaritai.auth.domain.User;
import com.gabaritai.auth.domain.UserStatus;
import com.gabaritai.auth.exception.InvalidOrExpiredTokenException;
import com.gabaritai.auth.messaging.NotificationEventPublisher;
import com.gabaritai.auth.repository.EmailConfirmationTokenRepository;
import com.gabaritai.auth.repository.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** UC-001 (confirmacao de e-mail) e seu fluxo alternativo de reenvio (RF01). */
@Service
public class EmailConfirmationService {

    private static final long TOKEN_TTL_HOURS = 24;

    private final EmailConfirmationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final SecureTokenGenerator tokenGenerator;
    private final NotificationEventPublisher notificationEventPublisher;

    public EmailConfirmationService(
            EmailConfirmationTokenRepository tokenRepository,
            UserRepository userRepository,
            SecureTokenGenerator tokenGenerator,
            NotificationEventPublisher notificationEventPublisher) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.notificationEventPublisher = notificationEventPublisher;
    }

    @Transactional
    public void issueToken(User user) {
        String rawToken = tokenGenerator.generateRawToken();
        Instant expiresAt = Instant.now().plus(TOKEN_TTL_HOURS, ChronoUnit.HOURS);
        EmailConfirmationToken token = new EmailConfirmationToken(
                UUID.randomUUID(), user.getId(), tokenGenerator.hash(rawToken), expiresAt);
        tokenRepository.save(token);
        notificationEventPublisher.publishEmailConfirmationRequested(user, rawToken, expiresAt);
    }

    @Transactional
    public void confirmEmail(String rawToken) {
        EmailConfirmationToken token = tokenRepository.findByTokenHash(tokenGenerator.hash(rawToken))
                .filter(t -> t.isValid(Instant.now()))
                .orElseThrow(InvalidOrExpiredTokenException::new);

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(InvalidOrExpiredTokenException::new);

        token.markUsed(Instant.now());
        user.activate();
        tokenRepository.save(token);
        userRepository.save(user);
    }

    /**
     * Reenvia o e-mail de confirmacao. Nao revela se o e-mail existe ou ja foi confirmado
     * (mesma politica anti-enumeracao usada em {@link PasswordResetService}).
     */
    @Transactional
    public void resendConfirmation(String email) {
        userRepository.findByEmailIgnoreCase(email)
                .filter(user -> user.getStatus() == UserStatus.PENDING_CONFIRMATION)
                .ifPresent(this::issueToken);
    }
}

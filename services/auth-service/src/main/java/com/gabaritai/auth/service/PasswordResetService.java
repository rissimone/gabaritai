package com.gabaritai.auth.service;

import com.gabaritai.auth.domain.PasswordResetToken;
import com.gabaritai.auth.domain.User;
import com.gabaritai.auth.domain.UserStatus;
import com.gabaritai.auth.exception.InvalidOrExpiredTokenException;
import com.gabaritai.auth.messaging.NotificationEventPublisher;
import com.gabaritai.auth.repository.PasswordResetTokenRepository;
import com.gabaritai.auth.repository.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** UC-003 — Recuperar senha. */
@Service
public class PasswordResetService {

    private static final long TOKEN_TTL_HOURS = 1;

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecureTokenGenerator tokenGenerator;
    private final NotificationEventPublisher notificationEventPublisher;

    public PasswordResetService(
            PasswordResetTokenRepository tokenRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            SecureTokenGenerator tokenGenerator,
            NotificationEventPublisher notificationEventPublisher) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
        this.notificationEventPublisher = notificationEventPublisher;
    }

    /**
     * Sempre retorna silenciosamente, exista ou nao o e-mail, para evitar enumeracao de contas
     * (RNF-032). O controller responde de forma identica em ambos os casos.
     */
    @Transactional
    public void requestReset(String email) {
        userRepository.findByEmailIgnoreCase(email)
                .filter(user -> user.getStatus() != UserStatus.BLOCKED)
                .ifPresent(user -> {
                    String rawToken = tokenGenerator.generateRawToken();
                    Instant expiresAt = Instant.now().plus(TOKEN_TTL_HOURS, ChronoUnit.HOURS);
                    PasswordResetToken token = new PasswordResetToken(
                            UUID.randomUUID(), user.getId(), tokenGenerator.hash(rawToken), expiresAt);
                    tokenRepository.save(token);
                    notificationEventPublisher.publishPasswordResetRequested(user, rawToken, expiresAt);
                });
    }

    @Transactional
    public void confirmReset(String rawToken, String newRawPassword) {
        PasswordResetToken token = tokenRepository.findByTokenHash(tokenGenerator.hash(rawToken))
                .filter(t -> t.isValid(Instant.now()))
                .orElseThrow(InvalidOrExpiredTokenException::new);

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(InvalidOrExpiredTokenException::new);

        token.markUsed(Instant.now());
        user.changePasswordHash(passwordEncoder.encode(newRawPassword));
        tokenRepository.save(token);
        userRepository.save(user);
    }
}

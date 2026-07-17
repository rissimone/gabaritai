package com.gabaritai.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabaritai.auth.domain.PasswordResetToken;
import com.gabaritai.auth.domain.User;
import com.gabaritai.auth.exception.InvalidOrExpiredTokenException;
import com.gabaritai.auth.messaging.NotificationEventPublisher;
import com.gabaritai.auth.repository.PasswordResetTokenRepository;
import com.gabaritai.auth.repository.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    private static final String EMAIL = "ana@example.com";

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecureTokenGenerator tokenGenerator;

    @Mock
    private NotificationEventPublisher notificationEventPublisher;

    @InjectMocks
    private PasswordResetService passwordResetService;

    private User existingUser() {
        return new User(UUID.randomUUID(), "Ana", EMAIL, "hashed-password", Instant.now());
    }

    @Test
    void requestReset_withExistingUser_persistsTokenAndPublishesEvent() {
        User user = existingUser();
        when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(user));
        when(tokenGenerator.generateRawToken()).thenReturn("raw-token");
        when(tokenGenerator.hash("raw-token")).thenReturn("hashed-token");

        passwordResetService.requestReset(EMAIL);

        ArgumentCaptor<PasswordResetToken> captor = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(tokenRepository).save(captor.capture());
        assertThat(captor.getValue().getTokenHash()).isEqualTo("hashed-token");
        verify(notificationEventPublisher).publishPasswordResetRequested(any(User.class), eq("raw-token"), any());
    }

    @Test
    void requestReset_withUnknownEmail_doesNothingSilently() {
        when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.empty());

        passwordResetService.requestReset(EMAIL);

        verify(tokenRepository, never()).save(any());
        verify(notificationEventPublisher, never()).publishPasswordResetRequested(any(), any(), any());
    }

    @Test
    void confirmReset_withValidToken_updatesPassword() {
        User user = existingUser();
        PasswordResetToken token = new PasswordResetToken(
                UUID.randomUUID(), user.getId(), "hashed-token", Instant.now().plus(1, ChronoUnit.HOURS));

        when(tokenGenerator.hash("raw-token")).thenReturn("hashed-token");
        when(tokenRepository.findByTokenHash("hashed-token")).thenReturn(Optional.of(token));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("NovaSenha123")).thenReturn("new-hashed-password");

        passwordResetService.confirmReset("raw-token", "NovaSenha123");

        assertThat(user.getPasswordHash()).isEqualTo("new-hashed-password");
        assertThat(token.getUsedAt()).isNotNull();
    }

    @Test
    void confirmReset_withExpiredToken_throws() {
        User user = existingUser();
        PasswordResetToken expiredToken = new PasswordResetToken(
                UUID.randomUUID(), user.getId(), "hashed-token", Instant.now().minus(1, ChronoUnit.HOURS));

        when(tokenGenerator.hash("raw-token")).thenReturn("hashed-token");
        when(tokenRepository.findByTokenHash("hashed-token")).thenReturn(Optional.of(expiredToken));

        assertThatThrownBy(() -> passwordResetService.confirmReset("raw-token", "NovaSenha123"))
                .isInstanceOf(InvalidOrExpiredTokenException.class);
    }

    @Test
    void confirmReset_withUnknownToken_throws() {
        when(tokenGenerator.hash("raw-token")).thenReturn("hashed-token");
        when(tokenRepository.findByTokenHash("hashed-token")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> passwordResetService.confirmReset("raw-token", "NovaSenha123"))
                .isInstanceOf(InvalidOrExpiredTokenException.class);
    }
}

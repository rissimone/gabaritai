package com.gabaritai.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabaritai.auth.domain.EmailConfirmationToken;
import com.gabaritai.auth.domain.User;
import com.gabaritai.auth.domain.UserStatus;
import com.gabaritai.auth.exception.InvalidOrExpiredTokenException;
import com.gabaritai.auth.messaging.NotificationEventPublisher;
import com.gabaritai.auth.repository.EmailConfirmationTokenRepository;
import com.gabaritai.auth.repository.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailConfirmationServiceTest {

    private static final String EMAIL = "ana@example.com";

    @Mock
    private EmailConfirmationTokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecureTokenGenerator tokenGenerator;

    @Mock
    private NotificationEventPublisher notificationEventPublisher;

    @InjectMocks
    private EmailConfirmationService emailConfirmationService;

    private User pendingUser() {
        return new User(UUID.randomUUID(), "Ana", EMAIL, "hashed-password", Instant.now());
    }

    @Test
    void confirmEmail_withValidToken_activatesUser() {
        User user = pendingUser();
        EmailConfirmationToken token = new EmailConfirmationToken(
                UUID.randomUUID(), user.getId(), "hashed-token", Instant.now().plus(1, ChronoUnit.HOURS));

        when(tokenGenerator.hash("raw-token")).thenReturn("hashed-token");
        when(tokenRepository.findByTokenHash("hashed-token")).thenReturn(Optional.of(token));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        emailConfirmationService.confirmEmail("raw-token");

        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(token.getUsedAt()).isNotNull();
    }

    @Test
    void confirmEmail_withExpiredToken_throws() {
        User user = pendingUser();
        EmailConfirmationToken expiredToken = new EmailConfirmationToken(
                UUID.randomUUID(), user.getId(), "hashed-token", Instant.now().minus(1, ChronoUnit.HOURS));

        when(tokenGenerator.hash("raw-token")).thenReturn("hashed-token");
        when(tokenRepository.findByTokenHash("hashed-token")).thenReturn(Optional.of(expiredToken));

        assertThatThrownBy(() -> emailConfirmationService.confirmEmail("raw-token"))
                .isInstanceOf(InvalidOrExpiredTokenException.class);
    }

    @Test
    void confirmEmail_withUnknownToken_throws() {
        when(tokenGenerator.hash("raw-token")).thenReturn("hashed-token");
        when(tokenRepository.findByTokenHash("hashed-token")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> emailConfirmationService.confirmEmail("raw-token"))
                .isInstanceOf(InvalidOrExpiredTokenException.class);
    }

    @Test
    void resendConfirmation_withPendingUser_issuesNewToken() {
        User user = pendingUser();
        when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(user));
        when(tokenGenerator.generateRawToken()).thenReturn("raw-token");
        when(tokenGenerator.hash("raw-token")).thenReturn("hashed-token");

        emailConfirmationService.resendConfirmation(EMAIL);

        verify(tokenRepository).save(any(EmailConfirmationToken.class));
        verify(notificationEventPublisher).publishEmailConfirmationRequested(any(User.class), any(), any());
    }

    @Test
    void resendConfirmation_withAlreadyActiveUser_doesNothing() {
        User user = pendingUser();
        user.activate();
        when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(user));

        emailConfirmationService.resendConfirmation(EMAIL);

        verify(tokenRepository, never()).save(any());
        verify(notificationEventPublisher, never()).publishEmailConfirmationRequested(any(), any(), any());
    }

    @Test
    void resendConfirmation_withUnknownEmail_doesNothingSilently() {
        when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.empty());

        emailConfirmationService.resendConfirmation(EMAIL);

        verify(tokenRepository, never()).save(any());
        verify(notificationEventPublisher, never()).publishEmailConfirmationRequested(any(), any(), any());
    }
}

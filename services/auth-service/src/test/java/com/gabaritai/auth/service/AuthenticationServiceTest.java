package com.gabaritai.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabaritai.auth.config.AccountLockProperties;
import com.gabaritai.auth.domain.User;
import com.gabaritai.auth.exception.AccountBlockedException;
import com.gabaritai.auth.exception.AccountLockedException;
import com.gabaritai.auth.exception.AccountNotConfirmedException;
import com.gabaritai.auth.exception.InvalidCredentialsException;
import com.gabaritai.auth.repository.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    private static final String EMAIL = "ana@example.com";
    private static final String RAW_PASSWORD = "Senha123";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private AccountLockProperties lockProperties;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        lockProperties = new AccountLockProperties();
        lockProperties.setMaxFailedAttempts(3);
        lockProperties.setLockDurationMinutes(15);
        authenticationService = new AuthenticationService(userRepository, passwordEncoder, jwtService, lockProperties);
    }

    private User activeUser() {
        return new User(UUID.randomUUID(), "Ana", EMAIL, "hashed-password", Instant.now());
    }

    @Test
    void login_withValidCredentials_returnsAccessToken() {
        User user = activeUser();
        user.activate();
        when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(RAW_PASSWORD, user.getPasswordHash())).thenReturn(true);
        when(jwtService.generateAccessToken(user)).thenReturn("jwt-token");

        String token = authenticationService.login(EMAIL, RAW_PASSWORD);

        assertThat(token).isEqualTo("jwt-token");
        assertThat(user.getFailedLoginAttempts()).isZero();
        verify(userRepository).save(user);
    }

    @Test
    void login_withUnknownEmail_throwsInvalidCredentialsWithoutRevealingCause() {
        when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.login(EMAIL, RAW_PASSWORD))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_withWrongPassword_incrementsFailedAttempts() {
        User user = activeUser();
        user.activate();
        when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(RAW_PASSWORD, user.getPasswordHash())).thenReturn(false);

        assertThatThrownBy(() -> authenticationService.login(EMAIL, RAW_PASSWORD))
                .isInstanceOf(InvalidCredentialsException.class);

        assertThat(user.getFailedLoginAttempts()).isEqualTo(1);
        verify(userRepository).save(user);
    }

    @Test
    void login_afterMaxFailedAttempts_locksAccount() {
        User user = activeUser();
        user.activate();
        when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(RAW_PASSWORD, user.getPasswordHash())).thenReturn(false);

        for (int i = 0; i < lockProperties.getMaxFailedAttempts(); i++) {
            assertThatThrownBy(() -> authenticationService.login(EMAIL, RAW_PASSWORD))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        assertThat(user.isLocked(Instant.now())).isTrue();
    }

    @Test
    void login_withLockedAccount_throwsAccountLocked() {
        User user = activeUser();
        user.activate();
        user.registerFailedLoginAttempt(1, Instant.now().plus(10, ChronoUnit.MINUTES));
        when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authenticationService.login(EMAIL, RAW_PASSWORD))
                .isInstanceOf(AccountLockedException.class);
    }

    @Test
    void login_withPendingConfirmation_throwsAccountNotConfirmed() {
        User user = activeUser();
        when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authenticationService.login(EMAIL, RAW_PASSWORD))
                .isInstanceOf(AccountNotConfirmedException.class);
    }

    @Test
    void login_withBlockedAccount_throwsAccountBlocked() {
        User user = activeUser();
        user.activate();
        user.block();
        when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authenticationService.login(EMAIL, RAW_PASSWORD))
                .isInstanceOf(AccountBlockedException.class);
    }
}

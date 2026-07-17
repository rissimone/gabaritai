package com.gabaritai.auth.service;

import com.gabaritai.auth.config.AccountLockProperties;
import com.gabaritai.auth.domain.User;
import com.gabaritai.auth.domain.UserStatus;
import com.gabaritai.auth.exception.AccountBlockedException;
import com.gabaritai.auth.exception.AccountLockedException;
import com.gabaritai.auth.exception.AccountNotConfirmedException;
import com.gabaritai.auth.exception.InvalidCredentialsException;
import com.gabaritai.auth.repository.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** UC-002 — Autenticar usuario (RF01), incluindo bloqueio por tentativas invalidas. */
@Service
@EnableConfigurationProperties(AccountLockProperties.class)
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AccountLockProperties lockProperties;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AccountLockProperties lockProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.lockProperties = lockProperties;
    }

    @Transactional
    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(InvalidCredentialsException::new);

        Instant now = Instant.now();
        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new AccountBlockedException();
        }
        if (user.isLocked(now)) {
            throw new AccountLockedException();
        }
        if (user.getStatus() == UserStatus.PENDING_CONFIRMATION) {
            throw new AccountNotConfirmedException();
        }

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            Instant lockUntil = now.plus(lockProperties.getLockDurationMinutes(), ChronoUnit.MINUTES);
            user.registerFailedLoginAttempt(lockProperties.getMaxFailedAttempts(), lockUntil);
            userRepository.save(user);
            throw new InvalidCredentialsException();
        }

        user.resetFailedLoginAttempts();
        userRepository.save(user);
        return jwtService.generateAccessToken(user);
    }
}

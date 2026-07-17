package com.gabaritai.auth.service;

import com.gabaritai.auth.domain.User;
import com.gabaritai.auth.exception.EmailAlreadyRegisteredException;
import com.gabaritai.auth.exception.TermsNotAcceptedException;
import com.gabaritai.auth.repository.UserRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** UC-001 — Cadastrar usuario (RF01). */
@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailConfirmationService emailConfirmationService;

    public RegistrationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailConfirmationService emailConfirmationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailConfirmationService = emailConfirmationService;
    }

    @Transactional
    public User register(String name, String email, String rawPassword, boolean termsAccepted) {
        if (!termsAccepted) {
            throw new TermsNotAcceptedException();
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyRegisteredException();
        }

        User user = new User(
                UUID.randomUUID(), name, email, passwordEncoder.encode(rawPassword), Instant.now());
        userRepository.save(user);
        emailConfirmationService.issueToken(user);
        return user;
    }
}

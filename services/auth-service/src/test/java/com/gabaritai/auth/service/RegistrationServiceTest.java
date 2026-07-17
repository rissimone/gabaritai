package com.gabaritai.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabaritai.auth.domain.User;
import com.gabaritai.auth.domain.UserStatus;
import com.gabaritai.auth.exception.EmailAlreadyRegisteredException;
import com.gabaritai.auth.exception.TermsNotAcceptedException;
import com.gabaritai.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailConfirmationService emailConfirmationService;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void register_withValidData_createsUserAndIssuesConfirmationToken() {
        when(userRepository.existsByEmailIgnoreCase("ana@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Senha123")).thenReturn("hashed-password");

        User user = registrationService.register("Ana", "ana@example.com", "Senha123", true);

        assertThat(user.getName()).isEqualTo("Ana");
        assertThat(user.getEmail()).isEqualTo("ana@example.com");
        assertThat(user.getPasswordHash()).isEqualTo("hashed-password");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING_CONFIRMATION);
        verify(userRepository).save(user);
        verify(emailConfirmationService).issueToken(user);
    }

    @Test
    void register_withoutAcceptingTerms_throwsAndDoesNotPersist() {
        assertThatThrownBy(() -> registrationService.register("Ana", "ana@example.com", "Senha123", false))
                .isInstanceOf(TermsNotAcceptedException.class);

        verify(userRepository, never()).save(any());
        verify(emailConfirmationService, never()).issueToken(any());
    }

    @Test
    void register_withEmailAlreadyRegistered_throwsAndDoesNotPersist() {
        when(userRepository.existsByEmailIgnoreCase("ana@example.com")).thenReturn(true);

        assertThatThrownBy(() -> registrationService.register("Ana", "ana@example.com", "Senha123", true))
                .isInstanceOf(EmailAlreadyRegisteredException.class);

        verify(userRepository, never()).save(any());
        verify(emailConfirmationService, never()).issueToken(any());
    }
}

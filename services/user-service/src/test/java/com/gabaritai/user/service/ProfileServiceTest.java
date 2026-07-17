package com.gabaritai.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabaritai.user.domain.ExperienceLevel;
import com.gabaritai.user.domain.LearningPreference;
import com.gabaritai.user.domain.UserProfile;
import com.gabaritai.user.exception.InvalidTimezoneException;
import com.gabaritai.user.repository.UserProfileRepository;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private UserProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void getOrCreateProfile_withExistingProfile_returnsIt() {
        UUID userId = UUID.randomUUID();
        UserProfile existing = new UserProfile(userId);
        when(profileRepository.findById(userId)).thenReturn(Optional.of(existing));

        UserProfile result = profileService.getOrCreateProfile(userId);

        assertThat(result).isSameAs(existing);
        verify(profileRepository, org.mockito.Mockito.never()).save(any());
    }

    @Test
    void getOrCreateProfile_withoutExistingProfile_createsWithDefaults() {
        UUID userId = UUID.randomUUID();
        when(profileRepository.findById(userId)).thenReturn(Optional.empty());
        when(profileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserProfile result = profileService.getOrCreateProfile(userId);

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getTimezone()).isEqualTo("America/Sao_Paulo");
        assertThat(result.isReceiveNotifications()).isTrue();
    }

    @Test
    void updateProfile_withValidData_updatesFields() {
        UUID userId = UUID.randomUUID();
        UserProfile existing = new UserProfile(userId);
        when(profileRepository.findById(userId)).thenReturn(Optional.of(existing));
        when(profileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Set<LearningPreference> preferences = EnumSet.of(LearningPreference.VIDEO, LearningPreference.QUESTOES);

        UserProfile result = profileService.updateProfile(
                userId,
                "https://example.com/foto.jpg",
                "+55 11 99999-0000",
                "America/Bahia",
                false,
                ExperienceLevel.INTERMEDIARIO,
                "Passar no concurso X",
                preferences);

        assertThat(result.getPhotoUrl()).isEqualTo("https://example.com/foto.jpg");
        assertThat(result.getTimezone()).isEqualTo("America/Bahia");
        assertThat(result.isReceiveNotifications()).isFalse();
        assertThat(result.getExperienceLevel()).isEqualTo(ExperienceLevel.INTERMEDIARIO);
        assertThat(result.getLearningPreferences()).containsExactlyInAnyOrder(
                LearningPreference.VIDEO, LearningPreference.QUESTOES);
    }

    @Test
    void updateProfile_withInvalidTimezone_throwsAndDoesNotSave() {
        UUID userId = UUID.randomUUID();

        assertThatThrownBy(() -> profileService.updateProfile(
                userId, null, null, "Nao/Existe", true, null, null, Set.of()))
                .isInstanceOf(InvalidTimezoneException.class);

        verify(profileRepository, org.mockito.Mockito.never()).save(any());
    }
}

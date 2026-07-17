package com.gabaritai.user.service;

import com.gabaritai.user.domain.ExperienceLevel;
import com.gabaritai.user.domain.LearningPreference;
import com.gabaritai.user.domain.UserProfile;
import com.gabaritai.user.exception.InvalidTimezoneException;
import com.gabaritai.user.repository.UserProfileRepository;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** UC-004 — Editar perfil (RF02). */
@Service
public class ProfileService {

    private final UserProfileRepository profileRepository;

    public ProfileService(UserProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional
    public UserProfile getOrCreateProfile(UUID userId) {
        return profileRepository.findById(userId).orElseGet(() -> profileRepository.save(new UserProfile(userId)));
    }

    @Transactional
    public UserProfile updateProfile(
            UUID userId,
            String photoUrl,
            String phone,
            String timezone,
            boolean receiveNotifications,
            ExperienceLevel experienceLevel,
            String studyGoal,
            Set<LearningPreference> learningPreferences) {
        validateTimezone(timezone);

        UserProfile profile = getOrCreateProfile(userId);
        profile.update(photoUrl, phone, timezone, receiveNotifications, experienceLevel, studyGoal, learningPreferences);
        return profileRepository.save(profile);
    }

    private void validateTimezone(String timezone) {
        try {
            ZoneId.of(timezone);
        } catch (DateTimeException e) {
            throw new InvalidTimezoneException(timezone);
        }
    }
}

package com.gabaritai.user.web.dto;

import com.gabaritai.user.domain.ExperienceLevel;
import com.gabaritai.user.domain.LearningPreference;
import com.gabaritai.user.domain.UserProfile;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record ProfileResponse(
        UUID userId,
        String photoUrl,
        String phone,
        String timezone,
        boolean receiveNotifications,
        ExperienceLevel experienceLevel,
        String studyGoal,
        Set<LearningPreference> learningPreferences,
        Instant updatedAt) {

    public static ProfileResponse from(UserProfile profile) {
        return new ProfileResponse(
                profile.getUserId(),
                profile.getPhotoUrl(),
                profile.getPhone(),
                profile.getTimezone(),
                profile.isReceiveNotifications(),
                profile.getExperienceLevel(),
                profile.getStudyGoal(),
                profile.getLearningPreferences(),
                profile.getUpdatedAt());
    }
}

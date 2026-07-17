package com.gabaritai.user.web.dto;

import com.gabaritai.user.domain.ExperienceLevel;
import com.gabaritai.user.domain.LearningPreference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record UpdateProfileRequest(
        @Size(max = 500) String photoUrl,
        @Size(max = 20) String phone,
        @NotBlank @Size(max = 50) String timezone,
        boolean receiveNotifications,
        ExperienceLevel experienceLevel,
        @Size(max = 500) String studyGoal,
        @NotNull Set<LearningPreference> learningPreferences) {
}

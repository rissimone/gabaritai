package com.gabaritai.user.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Perfil e preferencias do candidato (RF02, UC-004). {@code userId} e o mesmo identificador
 * gerado pelo auth-service no cadastro — nao ha chave estrangeira entre bancos (RNF-006).
 */
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false, length = 50)
    private String timezone;

    @Column(name = "receive_notifications", nullable = false)
    private boolean receiveNotifications;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", length = 30)
    private ExperienceLevel experienceLevel;

    @Column(name = "study_goal", length = 500)
    private String studyGoal;

    // EAGER: colecao pequena (no maximo 4 valores) sempre necessaria ao serializar o perfil.
    // O default LAZY quebraria a serializacao JSON, que ocorre no controller, fora da
    // transacao do service (LazyInitializationException apos a sessao Hibernate fechar).
    @ElementCollection(targetClass = LearningPreference.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_profile_learning_preferences", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "preference")
    private Set<LearningPreference> learningPreferences = EnumSet.noneOf(LearningPreference.class);

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(nullable = false)
    private long version;

    protected UserProfile() {
        // JPA
    }

    public UserProfile(UUID userId) {
        this.userId = userId;
        this.timezone = "America/Sao_Paulo";
        this.receiveNotifications = true;
    }

    public void update(
            String photoUrl,
            String phone,
            String timezone,
            boolean receiveNotifications,
            ExperienceLevel experienceLevel,
            String studyGoal,
            Set<LearningPreference> learningPreferences) {
        this.photoUrl = photoUrl;
        this.phone = phone;
        this.timezone = timezone;
        this.receiveNotifications = receiveNotifications;
        this.experienceLevel = experienceLevel;
        this.studyGoal = studyGoal;
        this.learningPreferences = EnumSet.noneOf(LearningPreference.class);
        this.learningPreferences.addAll(learningPreferences);
    }

    public UUID getUserId() {
        return userId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getPhone() {
        return phone;
    }

    public String getTimezone() {
        return timezone;
    }

    public boolean isReceiveNotifications() {
        return receiveNotifications;
    }

    public ExperienceLevel getExperienceLevel() {
        return experienceLevel;
    }

    public String getStudyGoal() {
        return studyGoal;
    }

    public Set<LearningPreference> getLearningPreferences() {
        return learningPreferences;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public long getVersion() {
        return version;
    }
}

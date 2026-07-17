export type ExperienceLevel = 'SEM_EXPERIENCIA' | 'INICIANTE' | 'INTERMEDIARIO' | 'EXPERIENTE';

export type LearningPreference = 'TEORIA' | 'QUESTOES' | 'REVISAO' | 'VIDEO';

export interface ProfileResponse {
  userId: string;
  photoUrl: string | null;
  phone: string | null;
  timezone: string;
  receiveNotifications: boolean;
  experienceLevel: ExperienceLevel | null;
  studyGoal: string | null;
  learningPreferences: LearningPreference[];
  updatedAt: string;
}

export interface UpdateProfileRequest {
  photoUrl: string | null;
  phone: string | null;
  timezone: string;
  receiveNotifications: boolean;
  experienceLevel: ExperienceLevel | null;
  studyGoal: string | null;
  learningPreferences: LearningPreference[];
}

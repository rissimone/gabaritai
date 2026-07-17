import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProfileService } from '../../core/services/profile.service';
import { extractErrorMessage } from '../../core/utils/api-error.util';
import { ExperienceLevel, LearningPreference, ProfileResponse } from '../../models/profile.models';

const TIMEZONES = [
  'America/Sao_Paulo',
  'America/Manaus',
  'America/Bahia',
  'America/Fortaleza',
  'America/Recife',
  'America/Belem',
  'America/Cuiaba',
  'America/Campo_Grande',
  'America/Boa_Vista',
  'America/Porto_Velho',
  'America/Rio_Branco',
  'America/Noronha',
];

const EXPERIENCE_LEVELS: { value: ExperienceLevel; label: string }[] = [
  { value: 'SEM_EXPERIENCIA', label: 'Sem experiencia' },
  { value: 'INICIANTE', label: 'Iniciante' },
  { value: 'INTERMEDIARIO', label: 'Intermediario' },
  { value: 'EXPERIENTE', label: 'Experiente' },
];

const LEARNING_PREFERENCES: { value: LearningPreference; label: string }[] = [
  { value: 'TEORIA', label: 'Teoria' },
  { value: 'QUESTOES', label: 'Questoes' },
  { value: 'REVISAO', label: 'Revisao' },
  { value: 'VIDEO', label: 'Video' },
];

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly profileService = inject(ProfileService);

  readonly timezones = TIMEZONES;
  readonly experienceLevels = EXPERIENCE_LEVELS;
  readonly learningPreferenceOptions = LEARNING_PREFERENCES;

  readonly loading = signal(true);
  readonly submitting = signal(false);
  readonly errorMessage = signal<string | null>(null);
  readonly successMessage = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    photoUrl: [''],
    phone: [''],
    timezone: ['America/Sao_Paulo', [Validators.required]],
    receiveNotifications: [true],
    experienceLevel: [''],
    studyGoal: [''],
    learningPreferences: this.fb.nonNullable.group({
      TEORIA: [false],
      QUESTOES: [false],
      REVISAO: [false],
      VIDEO: [false],
    }),
  });

  ngOnInit(): void {
    this.profileService.getProfile().subscribe({
      next: (profile) => this.populateForm(profile),
      error: (error) => {
        this.loading.set(false);
        this.errorMessage.set(extractErrorMessage(error));
      },
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    this.errorMessage.set(null);
    this.successMessage.set(null);

    const raw = this.form.getRawValue();
    this.profileService
      .updateProfile({
        photoUrl: raw.photoUrl || null,
        phone: raw.phone || null,
        timezone: raw.timezone,
        receiveNotifications: raw.receiveNotifications,
        experienceLevel: (raw.experienceLevel || null) as ExperienceLevel | null,
        studyGoal: raw.studyGoal || null,
        learningPreferences: this.selectedLearningPreferences(),
      })
      .subscribe({
        next: () => {
          this.submitting.set(false);
          this.successMessage.set('Perfil atualizado com sucesso!');
        },
        error: (error) => {
          this.submitting.set(false);
          this.errorMessage.set(extractErrorMessage(error));
        },
      });
  }

  private selectedLearningPreferences(): LearningPreference[] {
    const value = this.form.controls.learningPreferences.getRawValue();
    return (Object.keys(value) as LearningPreference[]).filter((key) => value[key]);
  }

  private populateForm(profile: ProfileResponse): void {
    this.form.patchValue({
      photoUrl: profile.photoUrl ?? '',
      phone: profile.phone ?? '',
      timezone: profile.timezone,
      receiveNotifications: profile.receiveNotifications,
      experienceLevel: profile.experienceLevel ?? '',
      studyGoal: profile.studyGoal ?? '',
    });
    for (const preference of profile.learningPreferences) {
      this.form.controls.learningPreferences.controls[preference].setValue(true);
    }
    this.loading.set(false);
  }
}

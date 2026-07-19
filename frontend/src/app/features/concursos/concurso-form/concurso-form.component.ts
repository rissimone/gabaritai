import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ConcursoService } from '../../../core/services/concurso.service';
import { extractErrorMessage } from '../../../core/utils/api-error.util';
import { EducationLevel } from '../../../models/concurso.models';

const EDUCATION_LEVELS: { value: EducationLevel; label: string }[] = [
  { value: 'FUNDAMENTAL', label: 'Ensino fundamental' },
  { value: 'MEDIO', label: 'Ensino medio' },
  { value: 'TECNICO', label: 'Ensino tecnico' },
  { value: 'SUPERIOR', label: 'Ensino superior' },
];

@Component({
  selector: 'app-concurso-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './concurso-form.component.html',
  styleUrl: './concurso-form.component.scss',
})
export class ConcursoFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly concursoService = inject(ConcursoService);
  private readonly router = inject(Router);

  readonly educationLevels = EDUCATION_LEVELS;
  readonly submitting = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    orgao: ['', [Validators.required, Validators.maxLength(200)]],
    bancaOrganizadora: ['', [Validators.required, Validators.maxLength(200)]],
    cargo: ['', [Validators.required, Validators.maxLength(200)]],
    numeroEdital: [''],
    dataProva: [''],
    escolaridade: [''],
    disciplinas: [''],
    quantidadeVagas: [''],
    linkOficial: [''],
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    this.errorMessage.set(null);

    const raw = this.form.getRawValue();
    const disciplinas = raw.disciplinas
      .split('\n')
      .map((linha) => linha.trim())
      .filter((linha) => linha.length > 0);

    this.concursoService
      .createConcurso({
        orgao: raw.orgao,
        bancaOrganizadora: raw.bancaOrganizadora,
        cargo: raw.cargo,
        numeroEdital: raw.numeroEdital || null,
        dataProva: raw.dataProva || null,
        escolaridade: (raw.escolaridade || null) as EducationLevel | null,
        disciplinas,
        quantidadeVagas: raw.quantidadeVagas ? Number(raw.quantidadeVagas) : null,
        linkOficial: raw.linkOficial || null,
        status: null,
      })
      .subscribe({
        next: () => {
          this.submitting.set(false);
          this.router.navigateByUrl('/concursos');
        },
        error: (error) => {
          this.submitting.set(false);
          this.errorMessage.set(extractErrorMessage(error));
        },
      });
  }
}

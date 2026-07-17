import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { extractErrorMessage } from '../../../core/utils/api-error.util';

const PASSWORD_PATTERN = /^(?=.*[A-Za-z])(?=.*\d).{8,}$/;

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.scss',
})
export class ResetPasswordComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly submitting = signal(false);
  readonly errorMessage = signal<string | null>(null);
  readonly successMessage = signal<string | null>(null);
  readonly token = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    newPassword: ['', [Validators.required, Validators.pattern(PASSWORD_PATTERN)]],
  });

  ngOnInit(): void {
    this.token.set(this.route.snapshot.queryParamMap.get('token'));
  }

  submit(): void {
    const token = this.token();
    if (!token || this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    this.errorMessage.set(null);

    this.authService
      .confirmPasswordReset({ token, newPassword: this.form.getRawValue().newPassword })
      .subscribe({
        next: () => {
          this.submitting.set(false);
          this.successMessage.set('Senha redefinida com sucesso!');
          setTimeout(() => this.router.navigateByUrl('/login'), 2000);
        },
        error: (error) => {
          this.submitting.set(false);
          this.errorMessage.set(extractErrorMessage(error));
        },
      });
  }
}

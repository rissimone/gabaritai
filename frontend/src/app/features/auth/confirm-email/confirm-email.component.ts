import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { extractErrorMessage } from '../../../core/utils/api-error.util';

type ConfirmationState = 'loading' | 'success' | 'error' | 'missing-token';

@Component({
  selector: 'app-confirm-email',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './confirm-email.component.html',
  styleUrl: './confirm-email.component.scss',
})
export class ConfirmEmailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly authService = inject(AuthService);

  readonly state = signal<ConfirmationState>('loading');
  readonly errorMessage = signal<string | null>(null);

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');
    if (!token) {
      this.state.set('missing-token');
      return;
    }

    this.authService.confirmEmail({ token }).subscribe({
      next: () => this.state.set('success'),
      error: (error) => {
        this.state.set('error');
        this.errorMessage.set(extractErrorMessage(error));
      },
    });
  }
}

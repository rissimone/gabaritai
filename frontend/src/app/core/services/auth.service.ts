import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  ConfirmEmailRequest,
  LoginRequest,
  LoginResponse,
  PasswordResetConfirmRequest,
  PasswordResetRequest,
  RegisterRequest,
  RegisterResponse,
  ResendConfirmationRequest,
} from '../../models/auth.models';

// RNF-123: evita localStorage (persistencia de longa duracao). O token vive em memoria
// (signal) e e replicado no sessionStorage apenas para sobreviver a um F5 na mesma aba;
// um cookie httpOnly seria mais seguro, mas exigiria o backend emitir cookies (fora de escopo agora).
const TOKEN_STORAGE_KEY = 'gabaritai.accessToken';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/auth`;

  private readonly tokenSignal = signal<string | null>(sessionStorage.getItem(TOKEN_STORAGE_KEY));

  readonly isAuthenticated = computed(() => this.tokenSignal() !== null);

  register(request: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.baseUrl}/register`, request);
  }

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.baseUrl}/login`, request)
      .pipe(tap((response) => this.setToken(response.accessToken)));
  }

  confirmEmail(request: ConfirmEmailRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/email-confirmations`, request);
  }

  resendConfirmation(request: ResendConfirmationRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/email-confirmations/resend`, request);
  }

  requestPasswordReset(request: PasswordResetRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/password-resets`, request);
  }

  confirmPasswordReset(request: PasswordResetConfirmRequest): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/password-resets`, request);
  }

  logout(): void {
    this.tokenSignal.set(null);
    sessionStorage.removeItem(TOKEN_STORAGE_KEY);
  }

  getToken(): string | null {
    return this.tokenSignal();
  }

  private setToken(token: string): void {
    this.tokenSignal.set(token);
    sessionStorage.setItem(TOKEN_STORAGE_KEY, token);
  }
}

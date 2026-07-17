export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  termsAccepted: boolean;
}

export interface RegisterResponse {
  id: string;
  name: string;
  email: string;
  status: 'PENDING_CONFIRMATION' | 'ACTIVE' | 'BLOCKED';
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
}

export interface ConfirmEmailRequest {
  token: string;
}

export interface ResendConfirmationRequest {
  email: string;
}

export interface PasswordResetRequest {
  email: string;
}

export interface PasswordResetConfirmRequest {
  token: string;
  newPassword: string;
}

/** Corpo de erro padrao (RFC 7807 / Spring ProblemDetail) devolvido pelo backend — RNF-122. */
export interface ApiProblemDetail {
  type?: string;
  title?: string;
  status?: number;
  detail?: string;
  instance?: string;
  errorCode?: string;
  correlationId?: string;
  fields?: string[];
}

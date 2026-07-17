import { HttpErrorResponse } from '@angular/common/http';
import { ApiProblemDetail } from '../../models/auth.models';

const FALLBACK_MESSAGE = 'Nao foi possivel completar a operacao. Tente novamente em instantes.';

/**
 * Extrai a mensagem amigavel do ProblemDetail (RFC 7807) devolvido pelo backend (RNF-122).
 * O backend ja evita expor detalhes tecnicos, entao normalmente basta exibir "detail".
 */
export function extractErrorMessage(error: unknown): string {
  if (error instanceof HttpErrorResponse) {
    const problem = error.error as ApiProblemDetail | undefined;
    if (problem?.detail) {
      return problem.detail;
    }
  }
  return FALLBACK_MESSAGE;
}

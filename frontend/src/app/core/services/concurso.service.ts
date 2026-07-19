import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  ConcursoResponse,
  ConcursoStatus,
  CriarConcursoRequest,
} from '../../models/concurso.models';

@Injectable({ providedIn: 'root' })
export class ConcursoService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/concursos`;

  listConcursos(status?: ConcursoStatus): Observable<ConcursoResponse[]> {
    const params = status ? new HttpParams().set('status', status) : undefined;
    return this.http.get<ConcursoResponse[]>(this.baseUrl, { params });
  }

  getConcurso(id: string): Observable<ConcursoResponse> {
    return this.http.get<ConcursoResponse>(`${this.baseUrl}/${id}`);
  }

  createConcurso(request: CriarConcursoRequest): Observable<ConcursoResponse> {
    return this.http.post<ConcursoResponse>(this.baseUrl, request);
  }
}

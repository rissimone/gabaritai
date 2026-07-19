import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../../environments/environment';
import { ConcursoService } from './concurso.service';

describe('ConcursoService', () => {
  let service: ConcursoService;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.apiBaseUrl}/concursos`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ConcursoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('lists concursos without a status filter', () => {
    service.listConcursos().subscribe();

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  it('lists concursos with a status filter as a query param', () => {
    service.listConcursos('PREVISTO').subscribe();

    const req = httpMock.expectOne(`${baseUrl}?status=PREVISTO`);
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  it('creates a concurso', () => {
    service
      .createConcurso({
        orgao: 'Tribunal X',
        bancaOrganizadora: 'Banca Y',
        cargo: 'Analista',
        numeroEdital: null,
        dataProva: null,
        escolaridade: null,
        disciplinas: [],
        quantidadeVagas: null,
        linkOficial: null,
        status: null,
      })
      .subscribe();

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });
});

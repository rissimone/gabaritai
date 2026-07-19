import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { environment } from '../../../../environments/environment';
import { ConcursoListComponent } from './concurso-list.component';

describe('ConcursoListComponent', () => {
  let component: ConcursoListComponent;
  let fixture: ComponentFixture<ConcursoListComponent>;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.apiBaseUrl}/concursos`;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConcursoListComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(ConcursoListComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('loads and displays the concursos on init', () => {
    fixture.detectChanges();

    httpMock.expectOne(baseUrl).flush([
      {
        id: '1',
        orgao: 'Tribunal X',
        bancaOrganizadora: 'Banca Y',
        cargo: 'Analista',
        numeroEdital: null,
        dataProva: null,
        escolaridade: null,
        disciplinas: [],
        quantidadeVagas: null,
        linkOficial: null,
        status: 'PREVISTO',
        createdAt: '2026-01-01T00:00:00Z',
        updatedAt: '2026-01-01T00:00:00Z',
      },
    ]);

    expect(component.loading()).toBeFalse();
    expect(component.concursos().length).toBe(1);
    expect(component.concursos()[0].orgao).toBe('Tribunal X');
  });

  it('reloads with a status query param when the filter changes', () => {
    fixture.detectChanges();
    httpMock.expectOne(baseUrl).flush([]);

    component.onFilterChange('PROVA_REALIZADA');

    const req = httpMock.expectOne(`${baseUrl}?status=PROVA_REALIZADA`);
    req.flush([]);
    expect(component.statusFilter()).toBe('PROVA_REALIZADA');
  });

  it('shows the backend error message when loading fails', () => {
    fixture.detectChanges();
    httpMock
      .expectOne(baseUrl)
      .flush(
        { detail: 'Identidade do usuario nao informada.' },
        { status: 401, statusText: 'Unauthorized' },
      );

    expect(component.errorMessage()).toBe('Identidade do usuario nao informada.');
  });
});

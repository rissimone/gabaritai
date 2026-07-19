import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { environment } from '../../../../environments/environment';
import { ConcursoFormComponent } from './concurso-form.component';

describe('ConcursoFormComponent', () => {
  let component: ConcursoFormComponent;
  let fixture: ComponentFixture<ConcursoFormComponent>;
  let httpMock: HttpTestingController;
  let router: Router;
  const baseUrl = `${environment.apiBaseUrl}/concursos`;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConcursoFormComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(ConcursoFormComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('does not submit when required fields are missing', () => {
    component.submit();

    httpMock.expectNone(() => true);
    expect(component.form.controls.orgao.touched).toBeTrue();
  });

  it('splits the disciplinas textarea into a list and navigates on success', () => {
    const navigateSpy = spyOn(router, 'navigateByUrl');
    component.form.setValue({
      orgao: 'Tribunal X',
      bancaOrganizadora: 'Banca Y',
      cargo: 'Analista',
      numeroEdital: '',
      dataProva: '',
      escolaridade: '',
      disciplinas: 'Direito Constitucional\nPortugues\n',
      quantidadeVagas: '',
      linkOficial: '',
    });

    component.submit();

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.body.disciplinas).toEqual(['Direito Constitucional', 'Portugues']);
    req.flush({});

    expect(navigateSpy).toHaveBeenCalledWith('/concursos');
  });

  it('shows the backend error message when creation fails', () => {
    component.form.setValue({
      orgao: 'Tribunal X',
      bancaOrganizadora: 'Banca Y',
      cargo: 'Analista',
      numeroEdital: '',
      dataProva: '',
      escolaridade: '',
      disciplinas: '',
      quantidadeVagas: '',
      linkOficial: '',
    });

    component.submit();

    const req = httpMock.expectOne(baseUrl);
    req.flush(
      { detail: 'Voce ja tem um concurso cadastrado com este orgao, cargo e banca.' },
      { status: 409, statusText: 'Conflict' },
    );

    expect(component.errorMessage()).toBe(
      'Voce ja tem um concurso cadastrado com este orgao, cargo e banca.',
    );
  });
});

import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { environment } from '../../../environments/environment';
import { ProfileComponent } from './profile.component';

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.apiBaseUrl}/usuarios/perfil`;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  function flushInitialLoad(overrides: Partial<Record<string, unknown>> = {}) {
    fixture.detectChanges();
    httpMock.expectOne(baseUrl).flush({
      userId: '1',
      photoUrl: null,
      phone: null,
      timezone: 'America/Bahia',
      receiveNotifications: false,
      experienceLevel: 'INTERMEDIARIO',
      studyGoal: 'Passar no concurso',
      learningPreferences: ['VIDEO', 'REVISAO'],
      updatedAt: '2026-01-01T00:00:00Z',
      ...overrides,
    });
  }

  it('should create and load the current profile into the form', () => {
    flushInitialLoad();

    expect(component.loading()).toBeFalse();
    expect(component.form.controls.timezone.value).toBe('America/Bahia');
    expect(component.form.controls.experienceLevel.value).toBe('INTERMEDIARIO');
    expect(component.form.controls.learningPreferences.controls.VIDEO.value).toBeTrue();
    expect(component.form.controls.learningPreferences.controls.TEORIA.value).toBeFalse();
  });

  it('sends the selected learning preferences and updated fields on submit', () => {
    flushInitialLoad();

    component.form.controls.studyGoal.setValue('Novo objetivo');
    component.submit();

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body.studyGoal).toBe('Novo objetivo');
    expect(req.request.body.learningPreferences.sort()).toEqual(['REVISAO', 'VIDEO']);
    req.flush({});

    expect(component.successMessage()).toContain('sucesso');
  });

  it('shows the backend error message when loading the profile fails', () => {
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

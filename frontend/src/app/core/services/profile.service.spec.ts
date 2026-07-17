import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../../environments/environment';
import { ProfileService } from './profile.service';

describe('ProfileService', () => {
  let service: ProfileService;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.apiBaseUrl}/usuarios/perfil`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ProfileService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('fetches the current profile', () => {
    service.getProfile().subscribe();

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush({
      userId: '1',
      photoUrl: null,
      phone: null,
      timezone: 'America/Sao_Paulo',
      receiveNotifications: true,
      experienceLevel: null,
      studyGoal: null,
      learningPreferences: [],
      updatedAt: '2026-01-01T00:00:00Z',
    });
  });

  it('sends an update request with the given payload', () => {
    service
      .updateProfile({
        photoUrl: null,
        phone: null,
        timezone: 'America/Bahia',
        receiveNotifications: false,
        experienceLevel: 'INICIANTE',
        studyGoal: null,
        learningPreferences: ['VIDEO'],
      })
      .subscribe();

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body.timezone).toBe('America/Bahia');
    req.flush({});
  });
});

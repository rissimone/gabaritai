import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../../environments/environment';
import { AuthService } from '../services/auth.service';
import { authInterceptor } from './auth.interceptor';

describe('authInterceptor', () => {
  let httpClient: HttpClient;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  beforeEach(() => {
    sessionStorage.clear();
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
      ],
    });
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    authService = TestBed.inject(AuthService);
  });

  afterEach(() => {
    httpMock.verify();
    sessionStorage.clear();
  });

  it('does not add an Authorization header when there is no token', () => {
    httpClient.get(`${environment.apiBaseUrl}/usuarios/me`).subscribe();

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/usuarios/me`);
    expect(req.request.headers.has('Authorization')).toBeFalse();
    req.flush({});
  });

  it('adds the Bearer token to requests targeting our own API', () => {
    authService.login({ email: 'ana@example.com', password: 'Senha123' }).subscribe();
    httpMock
      .expectOne(`${environment.apiBaseUrl}/auth/login`)
      .flush({ accessToken: 'jwt-token', tokenType: 'Bearer', expiresIn: 3600 });

    httpClient.get(`${environment.apiBaseUrl}/usuarios/me`).subscribe();

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/usuarios/me`);
    expect(req.request.headers.get('Authorization')).toBe('Bearer jwt-token');
    req.flush({});
  });

  it('does not add the token to requests outside our own API', () => {
    authService.login({ email: 'ana@example.com', password: 'Senha123' }).subscribe();
    httpMock
      .expectOne(`${environment.apiBaseUrl}/auth/login`)
      .flush({ accessToken: 'jwt-token', tokenType: 'Bearer', expiresIn: 3600 });

    httpClient.get('https://terceiros.example.com/recurso').subscribe();

    const req = httpMock.expectOne('https://terceiros.example.com/recurso');
    expect(req.request.headers.has('Authorization')).toBeFalse();
    req.flush({});
  });
});

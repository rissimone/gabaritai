import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../../environments/environment';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.apiBaseUrl}/auth`;

  beforeEach(() => {
    sessionStorage.clear();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    sessionStorage.clear();
  });

  it('starts unauthenticated when there is no stored token', () => {
    expect(service.isAuthenticated()).toBeFalse();
  });

  it('registers a new user', () => {
    service
      .register({
        name: 'Ana',
        email: 'ana@example.com',
        password: 'Senha123',
        termsAccepted: true,
      })
      .subscribe();

    const req = httpMock.expectOne(`${baseUrl}/register`);
    expect(req.request.method).toBe('POST');
    req.flush({ id: '1', name: 'Ana', email: 'ana@example.com', status: 'PENDING_CONFIRMATION' });
  });

  it('stores the access token and marks the user as authenticated after login', () => {
    service.login({ email: 'ana@example.com', password: 'Senha123' }).subscribe();

    const req = httpMock.expectOne(`${baseUrl}/login`);
    req.flush({ accessToken: 'jwt-token', tokenType: 'Bearer', expiresIn: 3600 });

    expect(service.isAuthenticated()).toBeTrue();
    expect(service.getToken()).toBe('jwt-token');
    expect(sessionStorage.getItem('gabaritai.accessToken')).toBe('jwt-token');
  });

  it('clears the token on logout', () => {
    service.login({ email: 'ana@example.com', password: 'Senha123' }).subscribe();
    httpMock
      .expectOne(`${baseUrl}/login`)
      .flush({ accessToken: 'jwt-token', tokenType: 'Bearer', expiresIn: 3600 });

    service.logout();

    expect(service.isAuthenticated()).toBeFalse();
    expect(sessionStorage.getItem('gabaritai.accessToken')).toBeNull();
  });
});

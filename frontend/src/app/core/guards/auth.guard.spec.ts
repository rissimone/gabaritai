import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { UrlTree, provideRouter } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { authGuard } from './auth.guard';

describe('authGuard', () => {
  let authService: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    sessionStorage.clear();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    });
    authService = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    sessionStorage.clear();
  });

  it('blocks access and redirects to /login when there is no token', () => {
    const result = TestBed.runInInjectionContext(() =>
      authGuard({} as never, { url: '/painel' } as never),
    );

    expect(result).toBeInstanceOf(UrlTree);
    expect((result as UrlTree).toString()).toBe('/login');
  });

  it('allows access when the user is authenticated', () => {
    authService.login({ email: 'ana@example.com', password: 'Senha123' }).subscribe();
    httpMock
      .expectOne((req) => req.url.endsWith('/auth/login'))
      .flush({ accessToken: 'jwt-token', tokenType: 'Bearer', expiresIn: 3600 });

    const result = TestBed.runInInjectionContext(() =>
      authGuard({} as never, { url: '/painel' } as never),
    );

    expect(result).toBeTrue();
  });
});

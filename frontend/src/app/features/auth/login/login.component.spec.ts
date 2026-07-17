import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';

import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('navigates to /painel after a successful login', () => {
    const navigateSpy = spyOn(router, 'navigateByUrl');
    component.form.setValue({ email: 'ana@example.com', password: 'Senha123' });

    component.submit();

    const req = httpMock.expectOne((r) => r.url.endsWith('/auth/login'));
    req.flush({ accessToken: 'jwt-token', tokenType: 'Bearer', expiresIn: 3600 });

    expect(navigateSpy).toHaveBeenCalledWith('/painel');
  });

  it('shows the backend error message on invalid credentials', () => {
    component.form.setValue({ email: 'ana@example.com', password: 'errada' });

    component.submit();

    const req = httpMock.expectOne((r) => r.url.endsWith('/auth/login'));
    req.flush(
      { detail: 'E-mail ou senha invalidos.' },
      { status: 401, statusText: 'Unauthorized' },
    );

    expect(component.errorMessage()).toBe('E-mail ou senha invalidos.');
  });
});

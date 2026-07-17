import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('does not submit when the form is invalid', () => {
    component.submit();

    httpMock.expectNone(() => true);
    expect(component.form.controls.name.touched).toBeTrue();
  });

  it('shows a success message after a valid submission', () => {
    component.form.setValue({
      name: 'Ana',
      email: 'ana@example.com',
      password: 'Senha123',
      termsAccepted: true,
    });

    component.submit();

    const req = httpMock.expectOne((r) => r.url.endsWith('/auth/register'));
    req.flush({ id: '1', name: 'Ana', email: 'ana@example.com', status: 'PENDING_CONFIRMATION' });

    expect(component.successMessage()).toContain('Cadastro realizado');
  });

  it('shows the backend error message when registration fails', () => {
    component.form.setValue({
      name: 'Ana',
      email: 'ana@example.com',
      password: 'Senha123',
      termsAccepted: true,
    });

    component.submit();

    const req = httpMock.expectOne((r) => r.url.endsWith('/auth/register'));
    req.flush(
      { detail: 'Ja existe uma conta cadastrada com este e-mail.' },
      { status: 409, statusText: 'Conflict' },
    );

    expect(component.errorMessage()).toBe('Ja existe uma conta cadastrada com este e-mail.');
  });
});

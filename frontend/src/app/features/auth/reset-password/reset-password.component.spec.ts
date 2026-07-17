import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ActivatedRoute, Router, convertToParamMap, provideRouter } from '@angular/router';

import { ResetPasswordComponent } from './reset-password.component';

describe('ResetPasswordComponent', () => {
  let component: ResetPasswordComponent;
  let fixture: ComponentFixture<ResetPasswordComponent>;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResetPasswordComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { queryParamMap: convertToParamMap({ token: 'abc123' }) } },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ResetPasswordComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  afterEach(() => httpMock.verify());

  it('should create and read the token from the URL', () => {
    expect(component.token()).toBe('abc123');
  });

  it('redirects to /login after successfully resetting the password', fakeAsync(() => {
    const navigateSpy = spyOn(router, 'navigateByUrl');
    component.form.setValue({ newPassword: 'NovaSenha123' });

    component.submit();

    const req = httpMock.expectOne((r) => r.url.endsWith('/auth/password-resets'));
    expect(req.request.method).toBe('PUT');
    req.flush(null);

    expect(component.successMessage()).toContain('sucesso');
    tick(2000);
    expect(navigateSpy).toHaveBeenCalledWith('/login');
  }));
});

import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap, provideRouter } from '@angular/router';

import { ConfirmEmailComponent } from './confirm-email.component';

function configureWithToken(token: string | null) {
  return TestBed.configureTestingModule({
    imports: [ConfirmEmailComponent],
    providers: [
      provideHttpClient(),
      provideHttpClientTesting(),
      provideRouter([]),
      {
        provide: ActivatedRoute,
        useValue: { snapshot: { queryParamMap: convertToParamMap(token ? { token } : {}) } },
      },
    ],
  }).compileComponents();
}

describe('ConfirmEmailComponent', () => {
  let component: ConfirmEmailComponent;
  let fixture: ComponentFixture<ConfirmEmailComponent>;
  let httpMock: HttpTestingController;

  afterEach(() => httpMock.verify());

  it('shows an error when there is no token in the URL', async () => {
    await configureWithToken(null);
    fixture = TestBed.createComponent(ConfirmEmailComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);

    fixture.detectChanges();

    expect(component.state()).toBe('missing-token');
  });

  it('confirms the email and shows success', async () => {
    await configureWithToken('abc123');
    fixture = TestBed.createComponent(ConfirmEmailComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);

    fixture.detectChanges();

    const req = httpMock.expectOne((r) => r.url.endsWith('/auth/email-confirmations'));
    expect(req.request.body).toEqual({ token: 'abc123' });
    req.flush(null);

    expect(component.state()).toBe('success');
  });

  it('shows the backend error when the token is invalid', async () => {
    await configureWithToken('abc123');
    fixture = TestBed.createComponent(ConfirmEmailComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);

    fixture.detectChanges();

    httpMock
      .expectOne((r) => r.url.endsWith('/auth/email-confirmations'))
      .flush({ detail: 'Token invalido ou expirado.' }, { status: 400, statusText: 'Bad Request' });

    expect(component.state()).toBe('error');
    expect(component.errorMessage()).toBe('Token invalido ou expirado.');
  });
});

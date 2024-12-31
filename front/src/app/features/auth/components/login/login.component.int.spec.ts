import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoginComponent } from './login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { expect, jest } from '@jest/globals';
import { Observable,of,throwError} from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

/** ------> integration test------
 * This test suite is an integration test for the `LoginComponent`, verifying its interactions with dependencies such as `AuthService`, `SessionService`, and the `Router`.
 * The necessary modules and services (e.g., `ReactiveFormsModule`, `HttpClientTestingModule`, `RouterTestingModule`) are imported and configured for the tests.
 * The tests simulate the submission of the login form with both valid and invalid data to ensure the component behaves as expected under different conditions.
 * The tests check whether the expected behaviors occur, such as successful login navigation or error handling when authentication fails.
 */
describe('LoginComponent (Integration Test)', () => {
  let loginComponent: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        ReactiveFormsModule,
        HttpClientModule,
        RouterTestingModule,
        HttpClientTestingModule,
        BrowserAnimationsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        SessionService,
        AuthService,
        {provide: Router,useValue: {navigate: jest.fn(),}},
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    loginComponent = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should successfully login and navigate to /sessions', () => {
    const sessionInformation$: Observable<SessionInformation> =of( {
      token: 'fake-token',
      type: 'admin',
      id: 1,
      username: 'aline@gmail.com',
      firstName: 'aline',
      lastName: 'doe',
      admin: true
    });

    // Simulate API response with an observable
    const loginSpy = jest.spyOn(authService, 'login').mockReturnValue(sessionInformation$);
    const sessionSpy = jest.spyOn(sessionService, 'logIn').mockImplementation(() => {});
    const navigateSpy = jest.spyOn(router, 'navigate');

    // Simulate form submission with valid data
    loginComponent.form.setValue({
      email: 'aline@gmail.com',
      password: 'password123'
    });

    loginComponent.submit();

    // Verification of expected effects
    expect(loginSpy).toHaveBeenCalled();
    expect(sessionSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onError to true if login fails', () => {
    // Simulate an authentication error
    const error = new Error('Authentication failed');
    const loginSpy = jest.spyOn(authService, 'login').mockReturnValue(throwError(() => error));
    
    // // Simulate form submission
    loginComponent.form.setValue({
      email: 'emailNotRegister@gmail.com',
      password: 'incorrectPassword'
    });

    loginComponent.submit();
    expect(loginSpy).toHaveBeenCalled();
    // Vérification que le flag d'erreur a été activé
    expect(loginComponent.onError).toBeTruthy();
  });
});

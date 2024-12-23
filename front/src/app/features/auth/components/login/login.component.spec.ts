import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service';
import { LoginComponent } from './login.component';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';




describe('LoginComponent', () => {
  let loginComponent: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const mockSessionInformation: SessionInformation = {
    token: '',
    type: '',
    id: 1,
    username: 'aline@gmail.com',
    firstName: 'aline',
    lastName: '',
    admin: true,
  };

  let mockRouter: Router;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockAuthService: jest.Mocked<AuthService>;

  mockRouter = {
    navigate: jest.fn(),
  } as unknown as jest.Mocked<Router>;

  mockAuthService = {
    login: jest.fn().mockReturnValue(of(undefined)),
  } as unknown as jest.Mocked<AuthService>;

  mockSessionService = {
    logIn: jest.fn().mockReturnValue(of(mockSessionInformation)),
  } as unknown as jest.Mocked<SessionService>;

  

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule],
        providers: [
          { provide: SessionService, useValue: mockSessionService },
          { provide: AuthService, useValue: mockAuthService },
          { provide: Router, useValue: mockRouter },
        ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    loginComponent = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(loginComponent).toBeTruthy();
  });

  describe('login component form', () => {
    it('should call sessionService.logIn()', () => {

      const logInSpySessionService = jest.spyOn(mockSessionService, 'logIn').mockImplementation(() => {});

      loginComponent.submit();

      expect(logInSpySessionService).toHaveBeenCalled;
 
    });
  
    it('should navigate to /sessions', () => {
      const navigateSpy = jest.spyOn(mockRouter, 'navigate');

      loginComponent.submit();

      expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
    });
  
    it('should set onError to true on error during login', () => {
      const error = new Error();

      const errorSpy = jest.spyOn(mockAuthService, 'login').mockReturnValueOnce(throwError(error));

      loginComponent.submit();

      expect(errorSpy).toHaveBeenCalled;
      expect(loginComponent.onError).toBeTruthy();
    });

  });

});

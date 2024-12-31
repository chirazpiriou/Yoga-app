import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthService } from '../../services/auth.service';
import { RegisterComponent } from './register.component';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { expect, jest } from '@jest/globals';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';

describe('RegisterComponent Integration Test', () => {
  let registerComponent: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        BrowserAnimationsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        AuthService,
        { provide: Router, useValue: { navigate: jest.fn() } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    registerComponent = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should successfully register and navigate to /login', () => {
    // Spy on the authService.register method and mock the return value
    const registerSpy = jest.spyOn(authService, 'register').mockReturnValue(of(void 0));
    const navigateSpy = jest.spyOn(router, 'navigate');

    // Fill out the form with valid data
    registerComponent.form.setValue({
      email: 'alinedupuis@gmail.com',
      firstName: 'aline',
      lastName: 'Dupuis',
      password: 'chats14'
    });

    // Simulate form submission
    registerComponent.submit();

    // Verify that the register method was called with correct form values
    expect(registerSpy).toHaveBeenCalledWith({
      email: 'alinedupuis@gmail.com',
      firstName: 'aline',
      lastName: 'Dupuis',
      password: 'chats14'
    });
    // Verify that navigation to /login was triggered after successful registration
    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
    // Ensure that the onError flag is not set after a successful registration
    expect(registerComponent.onError).toBeFalsy();
  });

  it('should set onError to true if registration fails', () => {
    // Simulate an error response from the register method
    const errorResponse = new Error('Registration failed');
    const registerError$ = throwError(() => errorResponse);

    // Spy on the authService.register method and mock the error response
    const registerSpy = jest.spyOn(authService, 'register').mockReturnValue(registerError$);

    // Fill out the form with valid data
    registerComponent.form.setValue({
      email: 'alinedupuis@gmail.com',
      firstName: 'aline',
      lastName: 'Dupuis',
      password: 'chats14'
    });

    // Simulate form submission
    registerComponent.submit();

    // Verify that the register method was called with correct form values
    expect(registerSpy).toHaveBeenCalledWith({
      email: 'alinedupuis@gmail.com',
      firstName: 'aline',
      lastName: 'Dupuis',
      password: 'chats14'
    });
    // Verify that the onError flag is set to true after a failed registration
    expect(registerComponent.onError).toBeTruthy();
  });
});

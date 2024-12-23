import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';

describe('RegisterComponent', () => {
  let registerComponent: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let mockAuthService: jest.Mocked<AuthService>;
  let mockRouter: jest.Mocked<Router>;

  beforeEach(async () => {
    // Mock AuthService
    mockAuthService = {
      register: jest.fn(),
    } as unknown as jest.Mocked<AuthService>;

    // Mock Router
    mockRouter = {
      navigate: jest.fn(),
    } as unknown as jest.Mocked<Router>;



    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        FormBuilder,
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    registerComponent = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(registerComponent).toBeTruthy();
  });

  it('should call register method when form is valid', () => {
    // Set valid form values
    registerComponent.form.setValue({
      email: 'aline@gmail.com',
      firstName: 'aline',
      lastName: 'Dupuis',
      password: 'chats147',
    });

    // Mock the register service to return a successful response
    mockAuthService.register.mockReturnValue(of(void 0));

    // Trigger submit method
    registerComponent.submit();

    // Expect the register method to be called with the correct data
    expect(mockAuthService.register).toHaveBeenCalledWith({
      email: 'aline@gmail.com',
      firstName: 'aline',
      lastName: 'Dupuis',
      password: 'chats147',
    });

    // Expect the router to navigate to login
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
  });


  it('should set onError to true when register fails', () => {
    // Set valid form values
    registerComponent.form.setValue({
      email: 'aline@gmail.com',
      firstName: 'aline',
      lastName: 'Dupuis',
      password: 'chats147',
    });

    // Mock the register service to return an error
    mockAuthService.register.mockReturnValue(throwError(() => new Error('Registration failed')));
    // Trigger submit method
    registerComponent.submit();
    // Expect onError to be set to true
    expect(registerComponent.onError).toBe(true);
  });












});

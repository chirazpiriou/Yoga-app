import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { SessionApiService } from '../../services/session-api.service';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { Session } from '../../interfaces/session.interface';
import { SessionInformation } from '../../../../interfaces/sessionInformation.interface';


import { ListComponent } from './list.component';
import { of } from 'rxjs';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  let mockSessionApiService: jest.Mocked<SessionApiService>;
  let mockSessionService: jest.Mocked<SessionService>;

  // Mock data
  const mockSession: Session = {
    id: 1,
    name: 'Session 1',
    description: 'Description of Session 1',
    date: new Date(),
    teacher_id: 2,
    users: [1],
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockSessionInformation: SessionInformation = {
    token: '123',
    type: 'admin',
    id: 1,
    username: 'user1',
    firstName: 'John',
    lastName: 'Doe',
    admin: true,
  };

  beforeEach(async () => {

    mockSessionApiService = {
      all: jest.fn().mockReturnValue(of([mockSession])),  // Simulate a call to `all()` that returns a session
    } as unknown as jest.Mocked<SessionApiService>;

    mockSessionService = {
      sessionInformation: mockSessionInformation,  // Set up the mock session information
    } as unknown as jest.Mocked<SessionService>;


    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch sessions on initialization', () => {
    // Check if `sessions$` is an observable
    component.sessions$.subscribe(sessions => {
      expect(sessions).toEqual([mockSession]);  // Verify the mock session data
    });

    // Ensure that the `all()` method of `sessionApiService` was called
    expect(mockSessionApiService.all).toHaveBeenCalled();
  });

  it('should return the correct session information from the user getter', () => {
    // Use the getter to check the user information
    expect(component.user).toEqual(mockSessionInformation);
  });

  it('should not return user information if it is undefined', () => {
    // Set the mock session information to undefined
    mockSessionService.sessionInformation = undefined;

    // Use the getter to check the user information
    expect(component.user).toBeUndefined();
  });


});

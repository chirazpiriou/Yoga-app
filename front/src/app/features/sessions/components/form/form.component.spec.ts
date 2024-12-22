import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { ActivatedRoute, Router } from '@angular/router';
import { TeacherService } from 'src/app/services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { Teacher } from 'src/app/interfaces/teacher.interface';

describe('FormComponent', () => {

  const mockSessionInformation: SessionInformation = { 
    token: '', 
    type: '', 
    id: 1, 
    username: '', 
    firstName: '', 
    lastName: '', 
    admin: true };

    const mockTeacher: Teacher = {
      id: 1,
      lastName: 'Dupuis',
      firstName: 'Janette',
      createdAt: new Date(), 
      updatedAt: new Date()};

  

  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  let mockRouter: jest.Mocked<Router>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;
  let mockActivatedRoute: jest.Mocked<ActivatedRoute>;
  let mockSessionApiService: jest.Mocked<SessionApiService>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockTeacherService: jest.Mocked<TeacherService>;
  let mockSession: jest.Mocked<Session>;

  beforeEach(async () => {

    mockSessionService = {
      sessionInformation: mockSessionInformation
    } as unknown as jest.Mocked<SessionService>;

    mockSession = {
      id: 2,
      name: 'Session 2',
      date: '',
      description: 'DESCRIPTION1',
      users: [0],
      teacher_id: 2
    } as unknown as jest.Mocked<Session>;

    mockRouter = {
      url: '/update/2',
      navigate: jest.fn()
    } as unknown as jest.Mocked<any>;

    mockMatSnackBar = {
      open: jest.fn()
    } as unknown as jest.Mocked<MatSnackBar>;

    mockSessionService = {
      sessionInformation: mockSessionInformation
    } as unknown as jest.Mocked<SessionService>;

    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of(mockSession)),
      create: jest.fn().mockReturnValue(of(mockSession)),
      update: jest.fn().mockReturnValue(of(mockSession)),
    } as unknown as jest.Mocked<SessionApiService>;

    mockTeacherService = {
      all: jest.fn().mockReturnValue(of([mockTeacher])),
    } as unknown as jest.Mocked<TeacherService>;

    mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('2'),
        },
      },
    } as unknown as jest.Mocked<ActivatedRoute>;

    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });



    describe('ngOnInit', () => {
      //Part 1
    it('should navigate to sessions page if admin is false ', () => {
      mockSessionService.sessionInformation = {...mockSessionInformation, admin: false};
      component.ngOnInit();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
      expect(mockRouter.navigate).toHaveBeenCalledTimes(1);
    });

    it('should not call navigate if sessionInformation is admin true', () => {
      mockSessionService.sessionInformation = {...mockSessionInformation, admin: true};
      component.ngOnInit();
      expect(mockRouter.navigate).toHaveBeenCalledTimes(0);
    });

    it('should set onUpdate to true, call sessionApiService.detail if url contains update', () => {
      // Initialize the `onUpdate` variable to false
      component.onUpdate = false;
    
      // Simulate the URL containing `/update/2`
      // This indicates that a request to update an item with ID 1 is being made

      // @ts-ignore --------------->is used here to ignore potential TypeScript errors
      mockRouter.url = '/update/2';
    
      // Spy on the `detail` method of the `mockSessionApiService` service
      // This allows checking if this method is called correctly later
      const detailSpy = jest.spyOn(mockSessionApiService, 'detail');
    
      // Spy on the `get` method of the `paramMap` from `mockActivatedRoute.snapshot`
      // This method is used to retrieve route parameters, such as the session ID
      const getSpy = jest.spyOn(mockActivatedRoute.snapshot.paramMap, 'get');
    
      // Spy on the `initForm` method of the component, which likely initializes a form
      // This allows checking if the method is called

      // @ts-ignore --------------->is also used here
      const initFormSpy = jest.spyOn(component, 'initForm');
    
      // Call the `ngOnInit()` method of the component to simulate the component's initialization
      component.ngOnInit();
    
      // Check that the `onUpdate` property has been correctly set to true
      expect(component.onUpdate).toBe(true);
    
      // Check that the `get` method of `paramMap` has been called
      expect(getSpy).toHaveBeenCalled();
    
      // Check that the session ID is correctly retrieved as '1'
      // This means the session ID was extracted from the URL

      // @ts-ignore --------------->is used here to force TypeScript to ignore the errors
      expect(component.id).toBe('2');
    
      // Check that the `detail` method of `mockSessionApiService` was called with '1' as a parameter
      expect(detailSpy).toHaveBeenCalledWith('2'); // Check that `component.id` is passed as a parameter
    
      // Check that the `initForm` method was called
      expect(initFormSpy).toHaveBeenCalled();
    });

    it('should call initForm if url does not contain update', () => {
      component.onUpdate = false;
      // @ts-ignore
      mockRouter.url = '/create';
      const detailSpy = jest.spyOn(mockSessionApiService, 'detail');
      // @ts-ignore
      const initFormSpy = jest.spyOn(component, 'initForm');

      component.ngOnInit();

      expect(component.onUpdate).toBe(false);
      expect(detailSpy).not.toHaveBeenCalled;
      expect(initFormSpy).toHaveBeenCalled;

      // initForm : initialize form with empty value
      expect(component.sessionForm?.get('name')?.value).toBe('');
      expect(component.sessionForm?.get('date')?.value).toBe('');
      expect(component.sessionForm?.get('teacher_id')?.value).toBe('');
      expect(component.sessionForm?.get('description')?.value).toBe('');
    });
      
  });
  
  describe('submit', () => {

    it('should call sessionApiService.create and exitPage("Session created !") if not onUpdate', () => {
      component.onUpdate = false;
      const createSpy = jest.spyOn(mockSessionApiService, 'create');
      // @ts-ignore
      const exitSpy = jest.spyOn(component, 'exitPage');

      component.submit();

      expect(createSpy).toHaveBeenCalled;
      expect(createSpy).toHaveBeenCalledTimes(1);
      expect(exitSpy).toHaveBeenCalledWith('Session created !');
      expect(exitSpy).toHaveBeenCalledTimes(1);
    });

    it('should call sessionApiService.update and exitPage("Session updated !") if onUpdate', () => {
      component.onUpdate = true;
      const updateSpy = jest.spyOn(mockSessionApiService, 'update');
      // @ts-ignore
      const exitSpy = jest.spyOn(component, 'exitPage');

      component.submit();
      expect(updateSpy).toHaveBeenCalled;
      expect(updateSpy).toHaveBeenCalledTimes(1);
      expect(exitSpy).toHaveBeenCalledWith('Session updated !');
      expect(exitSpy).toHaveBeenCalledTimes(1);
    });
  });

  describe('exitPage', () => {
    it('should call matSnackBar.open and router.navigate', () => {
      const openSpy = jest.spyOn(mockMatSnackBar, 'open');
      const navigateSpy = jest.spyOn(mockRouter, 'navigate');
      // @ts-ignore
      
      component.exitPage('test exitPage');
      expect(openSpy).toHaveBeenCalledWith('test exitPage', 'Close', { duration: 3000 });
      expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
    });
  }); 

});
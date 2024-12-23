import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder,ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';
import { DetailComponent } from './detail.component';

import { ActivatedRoute, Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from '../../../../interfaces/teacher.interface';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';



describe('DetailComponent', () => {
  let detailComponent: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  
  //Mock
  let mockSession: jest.Mocked<Session>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockTeacherService: jest.Mocked<TeacherService>;
  let mockActivatedRoute: jest.Mocked<ActivatedRoute>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;
  let mockRouter: jest.Mocked<Router>;
  let mockSessionApiService: jest.Mocked<SessionApiService>;

  const mockSessionInformation: SessionInformation = { token: '', type: '', id: 1, username: '', firstName: '', lastName: '', admin: true };
  const mockTeacher: Teacher = { id: 1, lastName: 'Dupuis', firstName: 'Janette', createdAt: new Date(), updatedAt: new Date()};
 

  beforeEach(async () => {

    mockSession = {
      id: 1,
      users: [1],
      teacher_id: 2
    } as unknown as jest.Mocked<Session>

    mockSessionService = {
      sessionInformation: mockSessionInformation
    } as unknown as jest.Mocked<SessionService>;

    mockTeacherService = {
      detail: jest.fn(),
    } as unknown as jest.Mocked<TeacherService>;

 
    mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1'),
        },
      },
    } as unknown as jest.Mocked<ActivatedRoute>;

    mockMatSnackBar = {
      open: jest.fn()
    } as unknown as jest.Mocked<MatSnackBar>;

    mockRouter = {
      navigate: jest.fn()
    } as unknown as jest.Mocked<Router>;
    
    mockSessionApiService = {
      delete: jest.fn(),
      participate: jest.fn(),
      unParticipate: jest.fn(),
      detail: jest.fn().mockReturnValue(of(mockSession))
    } as unknown as jest.Mocked<SessionApiService>;


    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatIconModule,
        MatCardModule 
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
        { provide: SessionApiService, useValue: mockSessionApiService },
      ],
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DetailComponent);
    detailComponent = fixture.componentInstance;
    fixture.detectChanges();
  });

  
  it('should create', () => {
    expect(detailComponent).toBeTruthy();
  });

  describe('participate', () => {
    it('should call sessionApiService.participate', () => {
      const SessionApiServiceSpy = jest.spyOn(mockSessionApiService, 'participate').mockReturnValue(of(void 0));
      
      detailComponent.participate();

      expect(SessionApiServiceSpy).toHaveBeenCalledWith(mockSession.id?.toString(), mockSessionInformation.id.toString());
  
    });
  });

  describe('unparticipate', () => {
    it('should call sessionApiService.unParticipate', () => {
      const SessionApiServiceSpy = jest.spyOn(mockSessionApiService, 'unParticipate').mockReturnValue(of(void 0));
      
      detailComponent.unParticipate();

      expect(SessionApiServiceSpy).toHaveBeenCalledWith(mockSession.id?.toString(), mockSessionInformation.id.toString());
  
    });
  });


  describe('Back', () => {
    it('should return to sessions page', () => {
      const spy = jest.spyOn(window.history, 'back');
      detailComponent.back();

      expect(spy).toHaveBeenCalled();
      expect(spy).toHaveBeenCalledTimes(1);
    });
  });

  describe('delete', () => {
    it('should delete the session by calling sessionApiService , display a message and then go back to session', () => {
      const SessionApiServiceSpy = jest.spyOn(mockSessionApiService, 'delete').mockReturnValue(of(null));
      
      detailComponent.delete();

      expect(SessionApiServiceSpy).toHaveBeenCalledWith('1');
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('ngOnInit', () => {
    it('should fetch the session information by calling sessionApiService.detail and teacherService.detail on ngOnInit', () => {
      const SessionApiServiceSpy = jest.spyOn(mockSessionApiService, 'detail').mockReturnValue(of(mockSession));
      const TeacherServiceSpy = jest.spyOn(mockTeacherService, 'detail').mockReturnValue(of(mockTeacher));

      detailComponent.ngOnInit();

      expect(SessionApiServiceSpy).toHaveBeenCalledWith('1');
      expect(TeacherServiceSpy).toHaveBeenCalledWith('2');
      expect(detailComponent.session).toEqual(mockSession);
      expect(detailComponent.teacher).toEqual(mockTeacher);
      expect(detailComponent.isParticipate).toBe(true);
    });
  });

  
});

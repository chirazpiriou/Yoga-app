import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { Session } from '../interfaces/session.interface';
import { HttpClientTestingModule,HttpTestingController,} from '@angular/common/http/testing';

import { SessionApiService } from './session-api.service';


/**------> unit test------
 * This test suite is a unit test for the SessionApiService.
 * It ensures that all methods in the service (e.g., all, detail, create, update, delete)
 * perform the correct HTTP requests and return the expected responses.
 * 
 * The HttpTestingController is used to mock HTTP interactions, simulating API calls
 * without connecting to a real backend.
 */
describe('SessionsService', () => {
  let sessionService: SessionApiService;
  let httpMock: HttpTestingController;
  let pathService = 'api/session';
  const sessionId = '1';

  const mockSession: Session = {
    id: 1,
    name: 'Yoga session',
    description: 'outdoor yoga',
    date: new Date('2024-12-21'),
    teacher_id: 5,
    users: [2, 3],
    createdAt: new Date('2024-12-01'),
    updatedAt: new Date('2024-12-10')
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule]
    });
    sessionService = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(sessionService).toBeTruthy();
  });

  describe('all', () => {
    it('should fetch all sessions', () => {
      const mockSessions: Session[] = [mockSession];

    sessionService.all().subscribe(sessions => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpMock.expectOne(pathService );
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);    
  });

  describe('detail', () => {
    it('should fetch session details by id', () => {
    sessionService.detail(sessionId).subscribe(session => {
      expect(session).toEqual(mockSession);
    });
    const req = httpMock.expectOne(`${pathService}/${sessionId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });    
  });

  describe('delete session', () => {
    it('should delete a session by id', () => {
      sessionService.delete(sessionId).subscribe(response => {
        expect(response).toBeTruthy();
      });
  
      const req = httpMock.expectOne(`${pathService}/${sessionId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush({});
    });    
  });

  describe('create session', () => {
    it('should create a new session', () => {
      sessionService.create(mockSession).subscribe(session => {
        expect(session).toEqual(mockSession);
      });
  
      const req = httpMock.expectOne(pathService);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockSession);
      req.flush(mockSession);
    });    
  });

  describe('update session', () => {
    it('should update an existing session', () => {
      const updatedSession = { ...mockSession, name: 'yoga night session' };
  
      sessionService.update(sessionId, updatedSession).subscribe(session => {
        expect(session).toEqual(updatedSession);
      });
  
      const req = httpMock.expectOne(`${pathService}/${sessionId}`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(updatedSession);
      req.flush(updatedSession);
    });    
  });

  describe('participate session', () => {
    const userId = '5';
    it('should participate a user in a session', () => {
      sessionService.participate(sessionId, userId).subscribe(() => {
        expect(true).toBe(true); // No content is returned, just ensure the call completes
      });
  
      const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toBeNull();
      req.flush({});
    });    
  });

  describe('participate session', () => {
    const userId = '5';
    it('should unparticipate a user from a session', () => {
      sessionService.unParticipate(sessionId, userId).subscribe(() => {
        expect(true).toBe(true); // No content is returned, just ensure the call completes
      });
  
      const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush({});
    });    
  });


});
});

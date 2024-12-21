import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let userService: UserService;
  let pathService = 'api/user';
  let httpMock: HttpTestingController; // Used to simulate HTTP requests
  const mockUser: User = { id: 1, email: '', lastName: '', firstName: '', admin: false, password: '', createdAt: new Date(), updatedAt: new Date()}; // A mock user for testing
  const userId = '1';
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[ HttpClientTestingModule]
    });
    userService = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Verifies that there are no unhandled HTTP requests
  });

  it('should be created', () => {
    expect(userService).toBeTruthy();
  });

  describe('getById', () => {
    it('should fetch user by ID', () => {
      
      userService.getById(userId).subscribe((user) => { // The subscribe is used to listen to the response to the HTTP call.
        expect(user).toEqual(mockUser); // We expect to receive the mock user
      });
     
      // Intercept the GET request and provide a simulated response
      const req = httpMock.expectOne(`${pathService}/${userId}`);
      expect(req.request.method).toBe('GET'); // Verifies that the HTTP method is GET
      req.flush(mockUser); // Respond to the request with mockUser
    });

    describe('delete', () => {
      it('should delete a user by ID', () => {
        userService.delete(userId).subscribe((response) => {
          expect(response).toBeNull(); // We expect the response to be null
        });
  
        const req = httpMock.expectOne(`${pathService}/${userId}`);
        expect(req.request.method).toBe('DELETE'); // Verifies that the HTTP method is DELETE
        req.flush(null); // Simulate the DELETE request response (no body, just the status)
      });
  
    });
  
  });























});

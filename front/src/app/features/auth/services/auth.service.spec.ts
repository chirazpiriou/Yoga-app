import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';



describe('AuthService', () => {
    let authService: AuthService;
    let httpMock: HttpTestingController;
    let pathService = 'api/auth';

    const mockRegisterRequest: RegisterRequest = {
        email: 'testuser@example.com',
        firstName: "aline",
        lastName: "dupont",
        password: "password12547"
      };

    const mockLoginRequest: LoginRequest = {
    email: 'alin@gmail.com',
    password: 'password123re'
    };

    const mockSessionInformation: SessionInformation = {
    token: 'fake-jwt-token',
    type: '',
    id: 1, 
    username: '', 
    firstName: '', 
    lastName: '', 
    admin: true
    };

    beforeEach(() => {
        TestBed.configureTestingModule({
          imports: [HttpClientTestingModule],
          providers: [AuthService]
        });
    
        authService = TestBed.inject(AuthService);
        httpMock = TestBed.inject(HttpTestingController);
      });
      afterEach(() => {
        httpMock.verify();  // Checks that no HTTP requests are pending
      });
       it('should be created', () => {
        expect(authService).toBeTruthy();
      });

      describe('register', () => {
        it('should send a POST request to register a user', () => {
          authService.register(mockRegisterRequest).subscribe();
    
          const req = httpMock.expectOne(`${pathService}/register`);
          expect(req.request.method).toBe('POST');
          expect(req.request.body).toEqual(mockRegisterRequest);
    
          req.flush(null); 
        });
      });


      describe('login', () => {
        it('should send a POST request to login a user and return session information', () => {
          authService.login(mockLoginRequest).subscribe((sessionInfo) => {
            expect(sessionInfo).toEqual(mockSessionInformation);
          });
    
          const req = httpMock.expectOne(`${pathService}/login`);
          expect(req.request.method).toBe('POST');
          expect(req.request.body).toEqual(mockLoginRequest);
          req.flush(mockSessionInformation);
        });
      });
}); 
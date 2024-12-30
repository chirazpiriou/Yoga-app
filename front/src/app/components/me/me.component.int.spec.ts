import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {Router} from '@angular/router';
import {SessionService} from '../../services/session.service';
import {UserService} from '../../services/user.service';
import {MeComponent} from './me.component';
import { expect, jest } from '@jest/globals';
import {of} from "rxjs";
import {SessionInformation} from "../../interfaces/sessionInformation.interface";
import {RouterTestingModule} from "@angular/router/testing";

describe('MeComponent Test Suites', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let matSnackBar: MatSnackBar;
  let sessionService: SessionService;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn()
  }

  class MockSnackBar {
    open() {
      return {
        onAction: () => of({}),
      };
    }
  }

  class MockRouter {
    get url(): string {
      return 'update';
    }

    navigate(): Promise<boolean> {
      return new Promise<boolean>((resolve, _) => resolve(true));
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        RouterTestingModule,
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        UserService,
        {provide: Router, useClass: MockRouter},
        {provide: SessionService, useValue: mockSessionService},
        {provide: MatSnackBar, useClass: MockSnackBar},
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should delete a user', () => {
    // Mock session information
    const mockSessionInformation: SessionInformation = {
      token: 'bearer token',
      type: 'jwt',
      id: 1,
      username: 'aline.dupuis@gmail.com',
      firstName: 'Aline',
      lastName: 'Dupuis',
      admin: false,
    };
    // Mock SessionInformation from SessionService
    sessionService.sessionInformation = mockSessionInformation;

    const spyUserServiceDelete = jest.spyOn(userService, 'delete').mockReturnValue(of({}));
    const spyMatSnackBar = jest.spyOn(matSnackBar, 'open');
    const spySessionServiceLogout = jest.spyOn(sessionService, 'logOut');
    const spyRouter = jest.spyOn(router, 'navigate');
    component.delete();
    // Verify that userService.delete was called
    expect(spyUserServiceDelete).toHaveBeenCalled();
    // Verify that matSnackBar.open was called with the correct parameters
    expect(spyMatSnackBar).toHaveBeenCalledWith('Your account has been deleted !', 'Close', {duration: 3000});
    // Verify that sessionService.logOut was called
    expect(spySessionServiceLogout).toHaveBeenCalled();
    // Verify that navigate to '/' was called
    expect(spyRouter).toHaveBeenCalledWith(['/']);
  })
});

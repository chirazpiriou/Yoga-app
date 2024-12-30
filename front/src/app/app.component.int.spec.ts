import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { AppComponent } from './app.component';
import { Router } from '@angular/router';
import {Observable, of } from 'rxjs';
import { SessionService } from './services/session.service';
import { AuthService } from './features/auth/services/auth.service';
import { ComponentFixture } from '@angular/core/testing';

describe('AppComponent (Integration Test)', () => {
  let fixture: ComponentFixture<AppComponent>;
  let appComponent: AppComponent;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([]),
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        AuthService, 
        SessionService
      ],
    }).compileComponents();

     // Service and component initialization
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(AppComponent);
    appComponent = fixture.componentInstance;
    //fixture.detectChanges()
  });

  it('should create the app', () => {  
    expect(appComponent).toBeTruthy();
  });

  it('should check $isLogged observable returns correct value', (done) => {
    // Mock of the value returned by $isLogged
    jest.spyOn(sessionService, '$isLogged').mockReturnValue(of(true));

    // Calling up the method and checking the result
    const result: Observable<boolean> = appComponent.$isLogged();
    result.subscribe(value => {
      expect(value).toBe(true);
      done(); //  Ensures that the test waits for completion of the observable
    });
  });

  it('should log out and navigate to the root route', () => {
    // Spies to observe method calls
    const spySessionService = jest.spyOn(sessionService, 'logOut');
    const spyRouter = jest.spyOn(router, 'navigate');

    // Calling the component's logout method
    appComponent.logout();

    // Checking service calls
    expect(spySessionService).toHaveBeenCalled();
    expect(spyRouter).toHaveBeenCalledWith(['']);
  });

});
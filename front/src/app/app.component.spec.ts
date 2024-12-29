import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { SessionService } from './services/session.service';
import { ComponentFixture} from '@angular/core/testing';

describe('AppComponent', () => {
  let appComponent: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockRouter: jest.Mocked<any>;


  mockSessionService = {
    $isLogged: jest.fn().mockImplementation(() => of(true)),
    logOut: jest.fn(),
  } as unknown as jest.Mocked<SessionService>;

  mockRouter = {
    navigate: jest.fn(),
  } as unknown as jest.Mocked<Router>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: RouterTestingModule, useValue: mockRouter },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    appComponent = fixture.componentInstance;
    fixture.detectChanges()
  });

  it('should create the app', () => {  
    expect(appComponent).toBeTruthy();
  });

  it('should call sessionService.$isLogged on $isLogged', () => {
    appComponent.$isLogged();
    expect(mockSessionService.$isLogged).toHaveBeenCalled();

  });

  it('should call sessionSerice.logout on logout', () => {
    const navigateSpy = jest.spyOn(mockRouter, 'navigate');
    appComponent.logout();
    expect(mockSessionService.logOut).toHaveBeenCalled();
    //expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
  });

});
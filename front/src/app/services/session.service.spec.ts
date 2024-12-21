import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { SessionService } from './session.service';
import { BehaviorSubject } from 'rxjs';
import {  Observable } from 'rxjs';

describe('SessionService', () => {
  let sessionService: SessionService;

  // Mock data conforming to the SessionInformation interface
  const mockSession: SessionInformation = {
    token: 'Token1548795',
    type: 'Bearer',
    id: 1,
    username: 'sarah@gmail.com',
    firstName: 'sarah',
    lastName: 'Dupuis',
    admin: true
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    sessionService = TestBed.inject(SessionService);
  });

  describe('SessionService Initial State', () => {
    it('should be created', () => {
      expect(sessionService).toBeTruthy();
    });

    it('should init  set isLogged to false', () => {
      expect(sessionService.isLogged).toBeFalsy();
    });

    it('should init set sessionInformation to undefined', () => {
      expect(sessionService.sessionInformation).toBeUndefined();
    });
  });

  describe('$isLogged', () => {
    it('should return an Observable for $isLogged()', () => {
      expect(sessionService.$isLogged()).toBeInstanceOf(Observable);
    });
  });

  describe('logIn', () => {
    it('should set isLogged to true and store session information', () => {
      sessionService.logIn(mockSession);

      expect(sessionService.isLogged).toBe(true); // Check that user is logged in
      expect(sessionService.sessionInformation).toEqual(mockSession); // Check that session information is set
    });

    it('should emit true on $isLogged observable after login', (done) => {
      sessionService.$isLogged().subscribe((isLogged) => {
        expect(isLogged).toBe(true); // Ensure observable emits true after login
        done();
      });

      sessionService.logIn(mockSession); // Call logIn to trigger observable emission
    }); 
  });

  describe('logOut', () => {
    it('should set isLogged to false and clear session information', () => {
      sessionService.logOut();
      expect(sessionService.isLogged).toBe(false);
      expect(sessionService.sessionInformation).toBeUndefined();
    });
  });










});

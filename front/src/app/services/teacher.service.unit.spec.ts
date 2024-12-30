import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { Teacher } from '../interfaces/teacher.interface';
import { TeacherService } from './teacher.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

/**------> unit test------
 * Unit tests for the `TeacherService` class to verify its interaction with HTTP endpoints.  
 * Tests include retrieving all teachers (`all`) and fetching teacher details by ID (`detail`).  
 * Mock data and HTTP responses are used to simulate API calls.  
 * Ensures correct HTTP methods and paths are used, and responses are properly handled.  
 */
describe('TeacherService (unit test)', () => {
  let teacherService: TeacherService;
  let httpMock: HttpTestingController;
  let pathService = 'api/teacher';
  const teacherId = '1';
  // Mock data conforming to the Teacher interface
  const mockTeachers: Teacher[] = [
    { id: 1, lastName: 'dubois', firstName: 'Janis', createdAt: new Date(), updatedAt: new Date() },
    { id: 2, lastName: 'duchamps', firstName: 'Jannette', createdAt: new Date(), updatedAt: new Date() }
  ];

  const mockTeacher: Teacher = { id: 1, lastName: 'dubois',firstName: 'janis',createdAt: new Date(),updatedAt: new Date() };


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule]
    });
    teacherService = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Verifies that there are no outstanding HTTP requests
  });

  it('should be created', () => {
    expect(teacherService).toBeTruthy();
  });

  describe('all', () => {
    it('should retrieve all teachers', () => {
      teacherService.all().subscribe((teachers) => {
        expect(teachers).toEqual(mockTeachers); // Check if the returned data matches mockTeachers
      });

      const req = httpMock.expectOne(pathService);
      expect(req.request.method).toBe('GET'); // Ensure the request method is GET
      req.flush(mockTeachers); // Provide the mock response
    });
  });

  describe('detail', () => {
    it('should retrieve teacher details by ID', () => {
      teacherService.detail(teacherId).subscribe((teacher) => {
        expect(teacher).toEqual(mockTeacher); // Check if the returned data matches mockTeacher
      });

      const req = httpMock.expectOne(`${pathService}/${teacherId}`);
      expect(req.request.method).toBe('GET'); // Ensure the request method is GET
      req.flush(mockTeacher); // Provide the mock response
    });
  });


});

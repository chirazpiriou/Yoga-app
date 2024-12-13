package com.openclassrooms.starterjwt.unitTests.Controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)  // Active Mockito for this test
@DisplayName("Teacher controller Unit test")
public class TeacherControllerUnitTests {
	
	
	@Mock // Create a TeacherMapper mock
    private TeacherMapper teacherMapper;
	
	@Mock // Create a UserService mock
    private TeacherService teacherService;
	
	
	@InjectMocks // Mockito here injects the mock into TeacherController 
    private TeacherController teacherController;
	
	
	private Teacher teacher;
	private TeacherDto teacherDto;
	
	@BeforeEach // This method is executed before each test to initialize the TeacherController  with a mocked TeacherService and TeacherMapper
	public void initialize() {
		teacherController = new TeacherController(teacherService, teacherMapper );
		

		Long teacherId = 3L;
		teacher = Teacher.builder()  // Mandatory because @NonNull fields
		                .id(teacherId)
		                .lastName("Dupont")               
		                .firstName("Arnaud")                              
		                .build();
	}
	
	@Test 
    @Tag("testFindTeacherById-api-teacher-id") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/teacher/{id} -> Find teacher by ID return teacher when teacher exists") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindTeacherById_shouldReturnTeacher_whenTeacherExists() {
        //Arrange: Prepare the test data and mock the necessary methods.
        
             
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(new TeacherDto());

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = teacherController.findById("3");

        //Assert: Verify that the method produces the expected result.
        verify(teacherService).findById(teacher.getId());
        verify(teacherMapper).toDto(teacher);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
	
	
	@Test 
    @Tag("testFindTeacherById-api-teacher-id") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/teacher/{id} -> Find teacher by ID return Not found when Teacher does not exists") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindTeacherById_shouldReturnNotFound_whenTeacherDoesNotExist() {
        //Arrange: Prepare the test data and mock the necessary methods.
        
             
        when(teacherService.findById(teacher.getId())).thenReturn(null);
      
        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = teacherController.findById("3");

        //Assert: Verify that the method produces the expected result.
        verify(teacherService).findById(teacher.getId());

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
	
	@Test 
    @Tag("testFindTeacherById-api-teacher-id") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/teacher/{id} -> Find teacher by ID return Bad request When Invalid Id ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindTeacherById_shouldReturnBadRequest_WhenInvalidId() {
        //Arrange: Prepare the test data and mock the necessary methods.
        
             
		String teacherInvalidID = "One";
      
        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = teacherController.findById(teacherInvalidID);

        //Assert: Verify that the method produces the expected result.
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
	
	@Test 
    @Tag("testFindAllTeachers-api-teacher-id") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/teacher -> Find teacher by ID return teacher when teacher exists") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindAllTeachers_shouldReturnAllTeachers() {
        //Arrange: Prepare the test data and mock the necessary methods.
		List<Teacher> teachers = new ArrayList<>();
		Teacher teacher_one = Teacher.builder()  // Mandatory because @NonNull fields
			                .id(1L)
			                .lastName("Woerth")               
			                .firstName("Lawrence")                              
			                .build();
		
		Teacher teacher_Two = Teacher.builder()  // Mandatory because @NonNull fields
			                .id(2L)
			                .lastName("Lechamps")               
			                .firstName("Alex")                              
			                .build();
		
		teachers.add(teacher_one);
        teachers.add(teacher_Two);
        
        TeacherDto teacherDtoOne = new TeacherDto(
        	    teacher_one.getId(), 
        	    teacher_one.getFirstName(), 
        	    teacher_one.getLastName(),
        	    null,  
        	    null 
        	);

		TeacherDto teacherDtoTwo = new TeacherDto(
		    teacher_Two.getId(), 
		    teacher_Two.getFirstName(), 
		    teacher_Two.getLastName(),
    	    null,  
    	    null 
		);
        
        List<TeacherDto> teachersDtos = new ArrayList<>(); 
        teachersDtos.add(teacherDtoOne);
        teachersDtos.add(teacherDtoTwo);
        
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teachersDtos);

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = teacherController.findAll();

        //Assert: Verify that the method produces the expected result.
        verify(teacherService).findAll();
        verify(teacherMapper).toDto(teachers);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        List<TeacherDto> responseBody = (List<TeacherDto>) result.getBody();
        assertEquals(2, responseBody.size());
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

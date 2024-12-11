package com.openclassrooms.starterjwt.unitTests.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;



@SpringBootTest
@ExtendWith(MockitoExtension.class)  // Active Mockito for this test
public class TeacherServiceUnitTest {
	@Mock // Create a UserRepository mock
    private TeacherRepository teacherRepository;
	
	@InjectMocks // Mockito here injects the mock into UserService
    private TeacherService teacherService;
	
	@BeforeEach // This method is executed before each test to initialize the UserService with a mocked UserRepository
	public void initialize() {
		teacherService = new TeacherService(teacherRepository);
	}
	
	
	@Test 
    @Tag("testFindTeacherById") // Allows you to group or filter tests by specific labels.
    @DisplayName("Find Teacher by ID return Teacher when Teacher exists") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindTeacherById_shouldReturnTeacher_whenTeacherExists() {
        //Arrange: Prepare the test data and mock the necessary methods.
        
		Long teacherId = 3L;
		Teacher teacher = Teacher.builder()  // Mandatory because @NonNull fields
		                .id(teacherId)
		                .lastName("Dupont")               
		                .firstName("Arnaud")                              
		                .build();
        
       
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        //Act: Call the method under test with the arranged data.
        Teacher result = teacherService.findById(teacherId);

        //Assert: Verify that the method produces the expected result.
        verify(teacherRepository).findById(teacherId);

        assertEquals(teacher, result);
    }
	
	
	@Test 
    @Tag("testFindTeacherById") // Allows you to group or filter tests by specific labels.
    @DisplayName("Find Teacher by ID return null when Teacher does not exists") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindTeacherById_shouldReturnNull_whenTeacherDoesNotExists() {
        //Arrange: Prepare the test data and mock the necessary methods.
        
		Long teacherId = 4L;
	
   
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        //Act: Call the method under test with the arranged data.
        Teacher result = teacherService.findById(teacherId);

        //Assert: Verify that the method produces the expected result.
        verify(teacherRepository).findById(teacherId);

        assertNull(result);
    }
	
	
	@Test 
    @Tag("testFindAllTeachers") // Allows you to group or filter tests by specific labels.
    @DisplayName("Find All Teachers") // Displays a more readable and descriptive name for each test, making them easier to understand.
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
       
        when(teacherRepository.findAll()).thenReturn(teachers);

        //Act: Call the method under test with the arranged data.
        List<Teacher> result = teacherService.findAll();

        //Assert: Verify that the method produces the expected result.
        verify(teacherRepository).findAll();

        assertEquals(teachers, result);
    }


}

package com.openclassrooms.starterjwt.unitTests.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;


@SpringBootTest
@ExtendWith(MockitoExtension.class)  // Active Mockito for this test
public class UserServiceUnitTest {
	
	@Mock // Create a UserRepository mock
    private UserRepository userRepository;
	
	@InjectMocks // Mockito here injects the mock into UserService
    private UserService userService;
	
	@BeforeEach // This method is executed before each test to initialize the UserService with a mocked UserRepository
	public void initialize() {
		userService = new UserService(userRepository);
	}
	
	
	@Test 
    @Tag("testFindUserById") // Allows you to group or filter tests by specific labels.
    @DisplayName("Find user by ID return user when User exists") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindUserById_shouldReturnUser_whenUserExists() {
        //Arrange: Prepare the test data and mock the necessary methods.
        
		Long userId = 1L;
        User user = User.builder()  // Mandatory because @NonNull fields
	                .id(userId)
	                .email("sophie@yogaapp.com")  
	                .lastName("Leclerc")               
	                .firstName("Sophie")              
	                .password("14chats")        
	                .admin(false)                   
	                .build();
        
       
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //Act: Call the method under test with the arranged data.
        User result = userService.findById(userId);

        //Assert: Verify that the method produces the expected result.
        verify(userRepository).findById(userId);

        assertEquals(user, result);
    }
	
	
	
	@Test 
    @Tag("testFindUserById") // Allows you to group or filter tests by specific labels.
    @DisplayName("Find user by ID return null when User does not exists") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindUserById_shouldReturnNull_whenUserDoesNotExist()
 {
        //Arrange: Prepare the test data and mock the necessary methods.
        
		Long userId = 5L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //Act: Call the method under test with the arranged data.
        User result = userService.findById(userId);

        //Assert: Verify that the method produces the expected result.
        verify(userRepository).findById(userId);

        assertNull(result);
    }
	
	
	@Test 
    @Tag("testDeleteUserById") // Allows you to group or filter tests by specific labels.
    @DisplayName("Delete user by ID ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testDeleteUserById()
 {
        //Arrange: Prepare the test data and mock the necessary methods.
        
		Long userId = 1L;
     

        //Act: Call the method under test with the arranged data.
        userService.delete(userId);

        //Assert: Verify that the method produces the expected result.
        verify(userRepository).deleteById(userId);
    }
}

package com.openclassrooms.starterjwt.unitTests.Controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)  // Active Mockito for this test
@DisplayName("User controller Unit test")
public class UserControllerUnitTests {
	
	@Mock // Create a UserMapper mock
    private UserMapper userMapper;
	
	@Mock // Create a UserService mock
    private UserService userService;
	
	@Mock
    private UserDetailsImpl userDetails; // Mock UserDetails
	
	@InjectMocks // Mockito here injects the mock into UserController 
    private UserController userController;
	
	
	private User user;
	
	@BeforeEach // This method is executed before each test to initialize the UserController  with a mocked UserService and UserMapper
	public void initialize() {
		userController = new UserController(userService, userMapper );
		
		Long userId = 1L;
        user = User.builder()  // Mandatory because @NonNull fields
	                .id(userId)
	                .email("sophie@yogaapp.com")  
	                .lastName("Leclerc")               
	                .firstName("Sophie")              
	                .password("14chats")        
	                .admin(false)                   
	                .build();
	}
	
	
	@Test 
    @Tag("testFindUserById-api-user-id") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/user/{id} -> Find user by ID return user when User exists") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindUserById_shouldReturnUser_whenUserExists() {
        //Arrange: Prepare the test data and mock the necessary methods.
        
             
        when(userService.findById(user.getId())).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new UserDto());

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = userController.findById("1");

        //Assert: Verify that the method produces the expected result.
        verify(userService).findById(user.getId());
        verify(userMapper).toDto(user);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
	
	
	
	@Test 
    @Tag("testFindUserById-api-user-id") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/user/{id} -> Find user by ID return Not found when User does not exists") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindUserById_shouldReturnNotFound_whenUserDoesNotExist()
 {
        //Arrange: Prepare the test data and mock the necessary methods.
        
	
        when(userService.findById(user.getId())).thenReturn(null);

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = userController.findById("1");

        //Assert: Verify that the method produces the expected result.
        verify(userService).findById(user.getId());

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
	
	@Test 
    @Tag("testFindUserById-api-user-id") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/user/{id} -> Find user by ID return Bad request When Invalid Id ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindUserById_shouldReturnBadRequest_WhenInvalidId()
 {
        //Arrange: Prepare the test data and mock the necessary methods.
        
		String userInvalidID = "One";
     

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = userController.findById(userInvalidID);

        //Assert: Verify that the method produces the expected result.
 
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

	
	
	@Test 
    @Tag("testDeleteUserById-api-user-id") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/user/{id} -> Delete user by ID return HttpStatus.OK when it succeeds ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testDeleteUserById_shouldReturnHttpStatusOK_whenSucceeds()
 {
		//Arrange: Prepare the test data and mock the necessary methods.
        
        when(userService.findById(user.getId())).thenReturn(user);
        when(userDetails.getUsername()).thenReturn(user.getEmail());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));
        
        

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = userController.save("1");

        //Assert: Verify that the method produces the expected result.
        verify(userService).delete(user.getId());
   
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
	
	@Test 
    @Tag("testDeleteUserById-api-user-id") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/user/{id} -> Delete user by ID return HttpStatus.UNAUTHORIZED when user Don't have the right to delete") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testDeleteUserById_shouldReturnHttpStatusUnauthorized()
 {
		//Arrange: Prepare the test data and mock the necessary methods.
        
        when(userService.findById(user.getId())).thenReturn(user);
        when(userDetails.getUsername()).thenReturn("differentUser@example.com");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));
        
        

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = userController.save("1");

        //Assert: Verify that the method produces the expected result.
   
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }
	
	
	
	
	@Test 
    @Tag("testDeleteUserById-api-user-id") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/user/{id} -> Delete user by ID return HttpStatus.NOT_FOUND when User does not exists") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testDeleteUserById_shouldReturnNotFound_whenUserDoesNotExist()
 {
		//Arrange: Prepare the test data and mock the necessary methods.
        when(userService.findById(user.getId())).thenReturn(null);
        
        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = userController.save("1");

        //Assert: Verify that the method produces the expected result.
  
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
	
	
	@Test 
    @Tag("testDeleteUserById-api-user-id") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/user/{id} -> Delete user by ID return HttpStatus.BAD_REQUEST When Invalid Id") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testDeleteUserById_shouldReturnBadRequest_whenInvalidId()
 {
		//Arrange: Prepare the test data and mock the necessary methods.
		String userInvalidID = "One";
        
        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = userController.save(userInvalidID);

        //Assert: Verify that the method produces the expected result.
  
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

}

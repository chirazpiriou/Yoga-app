package com.openclassrooms.starterjwt.unitTests.Controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)  // Active Mockito for this test
@DisplayName("Auth controller Unit test")
public class AuthControllerUnitTests {
	
	@Mock // Create a JwtUtils mock
    private JwtUtils jwtUtils;
	
	@Mock // Create a AuthenticationManager mock
    private AuthenticationManager authenticationManager;
	
	@Mock
    private PasswordEncoder passwordEncoder; // Mock PasswordEncoder
	
	
	@Mock
    private UserRepository userRepository; // Mock UserRepository
	
	
	
	@InjectMocks // Mockito here injects the mock into AuthController 
    private AuthController authController;
	
	
	private User user;
	
	private LoginRequest loginRequest;
	private UserDetailsImpl userDetails;
	private SignupRequest signUpRequest;
	
	
	@BeforeEach // This method is executed before each test to initialize the AuthController  with a mocked AuthenticationManager, PasswordEncoder, JwtUtils, UserRepository
	public void initialize() {
		authController = new AuthController( authenticationManager, passwordEncoder, jwtUtils, userRepository );
		
		loginRequest = new LoginRequest();
		loginRequest.setEmail("alice@gmail.com");
		loginRequest.setPassword("alice1547");
		
		signUpRequest = new SignupRequest();
		signUpRequest.setEmail("alex@gmail.com");
		signUpRequest.setPassword("alex1547");
		signUpRequest.setLastName("lebois");
        signUpRequest.setFirstName("alex");
		
		
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Dupont")
                .username("alice@gmail.com")
                .password("alice1547")
                .build();
        
        user = User.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Dupont")
                .email("alice@gmail.com")
                .password("alice1547")
                .admin(false)
                .build();
      
       
	
	}
	
	@Test 
    @Tag("testAuthenticateUser-api-auth-login") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/auth/login -> Test Authenticate User Should return Ok Response and JwtResponse when Credentials Are Valid") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testAuthenticateUser_shouldReturnOkResponseAndJwtResponse_whenCredentialsAreValid() {
        //Arrange: Prepare the test data and mock the necessary methods.
		Authentication authentication = mock(Authentication.class);
             
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtTestToken");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(user));
        

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = authController.authenticateUser(loginRequest);
        JwtResponse jwtResponse = (JwtResponse) result.getBody();

        //Assert: Verify that the method produces the expected result.
      

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("jwtTestToken", jwtResponse.getToken());
    }
	
	
	@Test 
    @Tag("testAuthenticateUser-api-auth-login") // Allows you to group or filter tests by specific labels.
   @DisplayName("/api/auth/login -> Test Authenticate User should Throw Exception when Authentication Fails") // Displays a more readable and descriptive name for each test, making them easier to understand.
   public void testAuthenticateUser_shouldThrowException_whenAuthenticationFails() {
        //Arrange: Prepare the test data and mock the necessary methods.

             
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException("Authentication failed"));
       
 
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> authController.authenticateUser(loginRequest));
        assertEquals("Authentication failed", exception.getMessage());
    }
	
	@Test 
    @Tag("testAuthenticateUser-api-auth-login") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/auth/login -> Test Authenticate User ,User is null") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testAuthenticateUser_UserIsNull() {
        //Arrange: Prepare the test data and mock the necessary methods.
		Authentication authentication = mock(Authentication.class);
             
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.empty());
        

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = authController.authenticateUser(loginRequest);
        JwtResponse jwtResponse = (JwtResponse) result.getBody();

        //Assert: Verify that the method produces the expected result.
      

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody() instanceof JwtResponse);
        assertNotNull(jwtResponse);
 
    }
	
	@Test 
    @Tag("testRegisterUser-api-auth-register") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/auth/register -> Test Register User Should return Ok Response") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testRegisterUser_shouldReturnOkResponse() {
        //Arrange: Prepare the test data and mock the necessary methods.
		when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
		when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());
    
        

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = authController.registerUser(signUpRequest);
    

        //Assert: Verify that the method produces the expected result.
  

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User registered successfully!", ((MessageResponse) result.getBody()).getMessage());

    }
	
	
	@Test 
    @Tag("testRegisterUser-api-auth-register") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/auth/register -> Test Register User Should return Bad Request Email is already taken ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testRegisterUser_shouldReturnBadRequestEmailAlreadyTaken() {
        //Arrange: Prepare the test data and mock the necessary methods.
		when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);
	
    
        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = authController.registerUser(signUpRequest);
    

        //Assert: Verify that the method produces the expected result.
  
        verify(userRepository, never()).save(any(User.class));
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertTrue(result.getBody() instanceof MessageResponse);
        assertEquals("Error: Email is already taken!", ((MessageResponse) result.getBody()).getMessage());

    }
	

}

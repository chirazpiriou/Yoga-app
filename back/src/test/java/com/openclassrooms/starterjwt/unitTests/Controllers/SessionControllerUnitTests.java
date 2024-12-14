package com.openclassrooms.starterjwt.unitTests.Controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.TeacherService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)  // Active Mockito for this test
@DisplayName("Session controller Unit test")


public class SessionControllerUnitTests {
	
	@Mock // Create a TeacherMapper mock
    private SessionMapper sessionMapper;
	
	@Mock // Create a UserService mock
    private SessionService sessionService;
	
	
	@InjectMocks // Mockito here injects the mock into SessionController 
    private SessionController sessionController;
	
	
	private Session session;
	private User user;
	private SessionDto sessionDto;
	
	@BeforeEach // This method is executed before each test to initialize the SessionController  with a mocked SessionService and SessionMapper
	public void initialize() {
		sessionController = new SessionController(sessionService, sessionMapper );
		
				
		LocalDateTime localDateTime =LocalDateTime.now();
		Date sessionDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

		session =  Session.builder()  // Mandatory because @NonNull fields
			                .id(3L)
			                .name("Morning Flow Yog") 
			                .date(sessionDate)              
			                .description("A dynamic morning flow to energize your body and mind. Suitable for all levels.")
			                .users(new ArrayList<>()) // Make sure the users list is initialized
			                .teacher(Teacher.builder().id(1L).build())
			                .build();
		
		
		Date sessionDtoDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		sessionDto = new SessionDto();
	        sessionDto.setId(3L);
	        sessionDto.setName("Morning Flow Yog");
	        sessionDto.setDate(sessionDtoDate);
	        sessionDto.setDescription("A dynamic morning flow to energize your body and mind. Suitable for all levels.");
	        sessionDto.setTeacher_id(1L);
      
		
		user = User.builder()
				.id(1L)
    			.email("fabien@yogaapp.com")  
                .lastName("Darvet")               
                .firstName("Fabien")              
                .password("15852chiens") 
                .admin(false)                   
                .build();
		   
	}
	
	@Test 
    @Tag("testFindSessionById-api-session-id") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/session/{id} -> Find session by ID return session when session exists") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindSessionById_shouldReturnSession_whenSessionExists() {
        //Arrange: Prepare the test data and mock the necessary methods.
		
             
        when(sessionService.getById(session.getId())).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(new SessionDto());

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = sessionController.findById(session.getId().toString());

        //Assert: Verify that the method produces the expected result.
        verify(sessionService).getById(session.getId());
        verify(sessionMapper).toDto(session);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(session.getId(), sessionDto.getId());
    }
	
	@Test 
    @Tag("testFindSessionById-api-session-id") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/session/{id} -> Find session by ID return Not Found when session null") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindSessionById_shouldReturnNotFoundSession_whenSessionNull() {
        //Arrange: Prepare the test data and mock the necessary methods.
        
             
        when(sessionService.getById(session.getId())).thenReturn(null);

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = sessionController.findById(session.getId().toString() );

        //Assert: Verify that the method produces the expected result.
        verify(sessionService).getById(session.getId());
       

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
	
	
	@Test 
    @Tag("testFindSessionById-api-session-id") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/session/{id} -> Find session by ID return Bad request when Invalid Id") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindSessionById_shouldReturnBadRequest_whenInvalidId() {
        
		//Arrange: Prepare the test data and mock the necessary methods.
		String sessionInvalidID = "Three";   
		
        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = sessionController.findById(sessionInvalidID );

        //Assert: Verify that the method produces the expected result.
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
	
	
	@Test 
    @Tag("testFindAllSessions") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/session ->Find All Sessions") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindAllSessions_shouldReturnAllSessions() {
        //Arrange: Prepare the test data and mock the necessary methods.
        
		
		List<Session> sessions = new ArrayList<>();
		sessions .add(session);
		sessions .add(session);
		
		LocalDateTime localDateTime = LocalDateTime.now();
		Date sessionDtoDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		
		SessionDto sessionDto = new SessionDto();
        sessionDto.setId(3L);
        sessionDto.setName("Morning Flow Yog");
        sessionDto.setDate(sessionDtoDate);
        sessionDto.setDescription("A dynamic morning flow to energize your body and mind. Suitable for all levels.");
        sessionDto.setTeacher_id(1L);
		
		
		List<SessionDto> sessionsDtos = new ArrayList<>(); 
		sessionsDtos.add(sessionDto);
		sessionsDtos.add(sessionDto);

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionsDtos);

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = sessionController.findAll();

        //Assert: Verify that the method produces the expected result.
        verify(sessionService).findAll();
        verify(sessionMapper).toDto(sessions);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        List<SessionDto> responseBody = (List<SessionDto>) result.getBody();
        assertEquals(2, responseBody.size());
    }
	
	
	@Test 
    @Tag("testCreateNewSession") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/session -> Create New session should Return Response Ok ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testCreateNewSession_shouldReturnResponseOk() {
        //Arrange: Prepare the test data and mock the necessary methods.
		
		when(sessionMapper.toEntity(sessionDto)).thenReturn(session);     
        when(sessionService.create(session)).thenReturn(session);
        

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = sessionController.create(sessionDto);

        //Assert: Verify that the method produces the expected result.
 
        verify(sessionMapper).toDto(session);

        assertEquals(HttpStatus.OK, result.getStatusCode());
     
    }
	
	@Test 
    @Tag("testUpdateSession") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/session/{id} -> Update session should Return Response Ok ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testUpdateSession_shouldReturnResponseOk() {
        //Arrange: Prepare the test data and mock the necessary methods.
		
		when(sessionMapper.toEntity(sessionDto)).thenReturn(session);     
        when(sessionService.update(session.getId(),session )).thenReturn(session);
        

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = sessionController.update( String.valueOf(session.getId()),sessionDto);

        //Assert: Verify that the method produces the expected result.
 
        verify(sessionMapper).toDto(session);

        assertEquals(HttpStatus.OK, result.getStatusCode());
     
    }
	
	@Test 
    @Tag("testUpdateSession") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/session/{id} -> Update session should Return Bad Request ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testUpdateSession_shouldReturnBadRequest() {
      
        

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = sessionController.update( "Id",sessionDto);

        //Assert: Verify that the method produces the expected result.
 

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
     
    }
	
	@Test 
    @Tag("testDeleteSessionById") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/session/{id} -> Delete session by Id should Return Ok response ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testDeleteSessionById_shouldReturnOkResponse() {
		//Arrange: Prepare the test data and mock the necessary methods.
		
		when(sessionService.getById(session.getId())).thenReturn(session);   
		doNothing().when(sessionService).delete(session.getId());
        

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = sessionController.save( String.valueOf(session.getId()));

        //Assert: Verify that the method produces the expected result.
        
        verify(sessionService, times(1)).delete(session.getId());
        assertEquals(HttpStatus.OK, result.getStatusCode());
     
    }
	
	@Test 
    @Tag("testDeleteSessionById") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/session/{id} -> Delete session by Id should Return Not Found ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testDeleteSessionById_shouldReturnNotFound() {
		//Arrange: Prepare the test data and mock the necessary methods.
		
		when(sessionService.getById(session.getId())).thenReturn(null);   
		
        

        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = sessionController.save( String.valueOf(session.getId()));

        //Assert: Verify that the method produces the expected result.
        
        verify(sessionService, times(0)).delete(session.getId());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
     
    }
	
	@Test 
    @Tag("testDeleteSessionById") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/session/{id} -> Delete session by Id should Return Bad Request ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testDeleteSessionById_shouldReturnBadRequest() {
		
		String invalidId = "Deux";
        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = sessionController.save( invalidId);

        //Assert: Verify that the method produces the expected result.
        
        verify(sessionService, times(0)).delete(session.getId());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
     
    }
	
	
	@Test 
    @Tag("testParticipateSession") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/session/{id}/participate/{userId} -> Participate to session should Return Ok response ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testParticipateSession_shouldReturnOkResponse() {
		
		//Arrange: Prepare the test data and mock the necessary methods.
		
		String sessionId = "1";
	    String userId = "2";

	    doNothing().when(sessionService).participate(Long.parseLong(sessionId), Long.parseLong(userId));  
	    
        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = sessionController.participate ( sessionId, userId);

        //Assert: Verify that the method produces the expected result.
        
        verify(sessionService, times(1)).participate(Long.parseLong(sessionId), Long.parseLong(userId));
        assertEquals(HttpStatus.OK, result.getStatusCode());
     
    }
	
	@Test 
    @Tag("testParticipateSession") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/session/{id}/participate/{userId} -> Participate to session should Return Bad Request for Invalid Id ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testParticipateSession_shouldReturnBadRequestInvalidId() {
		
		//Arrange: Prepare the test data and mock the necessary methods.
		
		String sessionId = "One";
	    String userId = "Two";

	    //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = sessionController.participate ( sessionId, userId);

        //Assert: Verify that the method produces the expected result.
        
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
     
    }
	
	@Test 
    @Tag("testNoLongerParticipateSession") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/session/{id}/participate/{userId} -> No Longer Participate to session should Return Ok response ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testNoLongerParticipateSession_shouldReturnOkResponse() {
		
		//Arrange: Prepare the test data and mock the necessary methods.
		
		String sessionId = "1";
	    String userId = "2";

	    doNothing().when(sessionService).noLongerParticipate(Long.parseLong(sessionId), Long.parseLong(userId));  
	    
        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = sessionController.noLongerParticipate ( sessionId, userId);

        //Assert: Verify that the method produces the expected result.
        
        verify(sessionService, times(1)).noLongerParticipate(Long.parseLong(sessionId), Long.parseLong(userId));
        assertEquals(HttpStatus.OK, result.getStatusCode());
     
    }
	
	@Test 
    @Tag("testNoLongerParticipateSession") // Allows you to group or filter tests by specific labels.
    @DisplayName("/api/session/{id}/participate/{userId} -> No Longer Participate to session should Return Bad Request for Invalid Id") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testNoLongerParticipateSession_shouldReturnBadRequestInvalidId() {
		
		//Arrange: Prepare the test data and mock the necessary methods.
		
		String sessionId = "One";
	    String userId = "Two";

	
	    
        //Act: Call the method under test with the arranged data.
        ResponseEntity<?> result = sessionController.noLongerParticipate ( sessionId, userId);

        //Assert: Verify that the method produces the expected result.
        
     
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
     
    }
	
	

}

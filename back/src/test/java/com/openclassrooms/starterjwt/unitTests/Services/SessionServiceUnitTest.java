package com.openclassrooms.starterjwt.unitTests.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.TeacherService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)  // Active Mockito for this test
public class SessionServiceUnitTest {
	
	
	@Mock // Create a SessionRepository mock
    private SessionRepository sessionRepository;
	
	@Mock // Create a UserRepository mock
	private  UserRepository userRepository;
	
	@InjectMocks // Mockito here injects the mock into SessionService
    private SessionService sessionService;
	
	private Session session; // Session declaration, at class level, for access in all tests
	private User user;
	
	@BeforeEach // This method is executed before each test to initialize the SessionService with a mocked SessionRepository and a mocked UserRepository
	public void initialize() {
		sessionService = new SessionService(sessionRepository, userRepository);
		
		
		LocalDateTime localDateTime =LocalDateTime.now();
		Date sessionDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

		session =  Session.builder()  // Mandatory because @NonNull fields
			                .id(3L)
			                .name("Morning Flow Yog") 
			                .date(sessionDate)              
			                .description("A dynamic morning flow to energize your body and mind. Suitable for all levels.")
			                .users(new ArrayList<>()) // Make sure the users list is initialized
			                .build();
      
		
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
    @Tag("testGetSessionById") // Allows you to group or filter tests by specific labels.
    @DisplayName("Get Session by ID return Session when Session exists") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testGetSessionById_shouldReturnSession_whenSessionExists()  {
        //Arrange: Prepare the test data and mock the necessary methods.
        
		
	
       
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        //Act: Call the method under test with the arranged data.
        Session result = sessionService.getById(session.getId());

        //Assert: Verify that the method produces the expected result.
        verify(sessionRepository).findById(session.getId());

        assertEquals(session, result);
    }
	
	@Test 
    @Tag("testGetSessionById") // Allows you to group or filter tests by specific labels.
    @DisplayName("Get Session by ID return null when Session does not exists") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testGetSessionById_shouldReturnnull_whenSessionDoesNotExists()  {
        //Arrange: Prepare the test data and mock the necessary methods.
              
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        //Act: Call the method under test with the arranged data.
        Session result = sessionService.getById(session.getId());

        //Assert: Verify that the method produces the expected result.
        verify(sessionRepository).findById(session.getId());

        assertNull( result);
    }
	
	@Test 
    @Tag("testCreateSession") // Allows you to group or filter tests by specific labels.
    @DisplayName("Create Session ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testCreateSession()  {
        //Arrange: Prepare the test data and mock the necessary methods.
              
        when(sessionRepository.save(session)).thenReturn(session);

        //Act: Call the method under test with the arranged data.
        Session result = sessionService.create(session);

        //Assert: Verify that the method produces the expected result.
        verify(sessionRepository).save(session);

        assertEquals(session, result);
    }
	
	@Test 
    @Tag("testDeleteSessionById") // Allows you to group or filter tests by specific labels.
    @DisplayName("Delete Session by Id ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testDeleteSessionById()  {
		//Arrange: Prepare the test data and mock the necessary methods.
		doNothing().when(sessionRepository).deleteById(session.getId()); // doNothing() is useful for void methods that have no return value
        //Act: Call the method under test with the arranged data.
        sessionService.delete(session.getId());

        //Assert: Verify that the method produces the expected result.
        verify(sessionRepository, times(1)).deleteById(session.getId());

 
    }
	
	@Test 
    @Tag("testFindAllSessions") // Allows you to group or filter tests by specific labels.
    @DisplayName("Find all Sessions ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testFindAllSessions()  {
        //Arrange: Prepare the test data and mock the necessary methods.
		
		List<Session> sessions = new ArrayList<>();
		sessions .add(session);
		sessions .add(session);
              
        when(sessionRepository.findAll()).thenReturn(sessions);

        //Act: Call the method under test with the arranged data.
        List<Session> result = sessionService.findAll();

        //Assert: Verify that the method produces the expected result.
        verify(sessionRepository).findAll();

        assertEquals(sessions, result);
    }
	
	@Test 
    @Tag("testUpdateSession") // Allows you to group or filter tests by specific labels.
    @DisplayName("Update Session ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testUpdateSession()  {
        //Arrange: Prepare the test data and mock the necessary methods.
		Session updatedSession =  Session.builder()  // Mandatory because @NonNull fields
				                .name("Energitic Yog")             
				                .description("An energizing morning yoga session to awaken your body and mind") 
				                .date(session.getDate()) // Uses the same date as the original session date
				                .build(); 
              
        when(sessionRepository.save(updatedSession)).thenReturn(updatedSession);

        //Act: Call the method under test with the arranged data.
        Session result = sessionService.update(session.getId(), updatedSession);

        //Assert: Verify that the method produces the expected result.
        verify(sessionRepository).save(updatedSession);

        assertEquals(updatedSession, result);
    }
	
	@Test 
    @Tag("testUserParticipateToSessionWithSucess") // Allows you to group or filter tests by specific labels.
    @DisplayName("User Participate To Session With Sucess ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testUserParticipateToSessionWithSucess()  {
        //Arrange: Prepare the test data and mock the necessary methods.
	
              
		when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));


        //Act: Call the method under test with the arranged data.
        sessionService.participate(session.getId(), user.getId());

        //Assert: Verify that the method produces the expected result.
        verify(sessionRepository).save(session);

        assertTrue(session.getUsers().contains(user));
    }
	
	@Test 
    @Tag("testUserParticipateToSessionForUserNotFound") // Allows you to group or filter tests by specific labels.
    @DisplayName("User Not Found Can Not Participate To Session ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testUserParticipateToSessionForUserNotFound()  {
        //Arrange: Prepare the test data and mock the necessary methods.
	
              
		when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
		when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        //Assert: Verify that the method produces the expected result.
        assertThrows(com.openclassrooms.starterjwt.exception.NotFoundException.class, () ->  sessionService.participate(session.getId(), user.getId()));
        
       
    }
	
	@Test 
    @Tag("testUserParticipateToSessionForSessionNotFound") // Allows you to group or filter tests by specific labels.
    @DisplayName("Session Not Found Can Not Participate To Session") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testUserParticipateToSessionForSessionNotFound()  {
        //Arrange: Prepare the test data and mock the necessary methods.
	
              
		when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));


        //Assert: Verify that the method produces the expected result.

		assertThrows(com.openclassrooms.starterjwt.exception.NotFoundException.class, () ->  sessionService.participate(session.getId(), user.getId()));
    }
	
	@Test 
    @Tag("testUserCanNotParticipateToSessionUserAlreadyParticipate") // Allows you to group or filter tests by specific labels.
    @DisplayName("User Can Not Participate To Session Already Participate") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testUserCanNotParticipateToSessionUserAlreadyParticipate()  {
        //Arrange: Prepare the test data and mock the necessary methods.
	
		session.getUsers().add(user);      
		when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        //Assert: Verify that the method produces the expected result.

		assertThrows(com.openclassrooms.starterjwt.exception.BadRequestException.class, () ->  sessionService.participate(session.getId(), user.getId()));
		verify(sessionRepository, times(0)).save(session); // Check that the save method has not been called because adding fails
    } 
	
	
	
	@Test 
    @Tag("testUserCancelParticipationToSessionWithSucess") // Allows you to group or filter tests by specific labels.
    @DisplayName("User Cancel Participation To Session With Sucess ") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testUserCancelParticipationToSessionWithSucess()  {
        //Arrange: Prepare the test data and mock the necessary methods.
	
		      
		when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
		session.getUsers().add(user);
		when(sessionRepository.save(session)).thenReturn(session);
		

		
        //Act: Call the method under test with the arranged data.
        sessionService.noLongerParticipate(session.getId(), user.getId());

        //Assert: Verify that the method produces the expected result.
        verify(sessionRepository).save(session);

        assertFalse(session.getUsers().contains(user)); //User should no longer be in the session
    }
	
	@Test 
    @Tag("testUserCanNotCancelParticipationToSessionForSessionNotFound") // Allows you to group or filter tests by specific labels.
    @DisplayName("Session Not Found Can Not Cancel Participation To Session") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testUserCanNotCancelParticipationToSessionForSessionNotFound()  {
        //Arrange: Prepare the test data and mock the necessary methods.
	
              
		when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        //Assert: Verify that the method produces the expected result.

		assertThrows(com.openclassrooms.starterjwt.exception.NotFoundException.class, () ->  sessionService.noLongerParticipate(session.getId(), user.getId()));
    }
	
	@Test 
    @Tag("testUserCanNotCancelParticipationToSessionUserHaveNotParticipate") // Allows you to group or filter tests by specific labels.
    @DisplayName("User Can Not Cancel Participation To Session User Have Not Participate") // Displays a more readable and descriptive name for each test, making them easier to understand.
    public void testUserCanNotCancelParticipationToSessionUserHaveNotParticipate()  {
        //Arrange: Prepare the test data and mock the necessary methods.
	
		session.getUsers().clear();      
		when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        //Assert: Verify that the method produces the expected result.

		assertThrows(com.openclassrooms.starterjwt.exception.BadRequestException.class, () ->  sessionService.noLongerParticipate(session.getId(), user.getId()));
		
    } 



}
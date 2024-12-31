package com.openclassrooms.starterjwt.integrationTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Session Controller Integration Tests")
public class SessionControllerIntegrationTests {
	
	@Autowired
    private MockMvc mockMvc;
	
	private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JwtUtils jwtUtils;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    @Value("${oc.app.jwtExpirationMs}")
    private long jwtExpirationMs;

    private String jwtToken;
    private User user;
    private Session sessionOne ;
    private Session sessionTwo ;
    private SessionDto sessionDto;

    @BeforeEach
    void setUp() {
    	 user = new User();
         user.setId(1L);
         user.setEmail("alice@gmail.com");
         user.setPassword("chats147");
  
        jwtToken = Jwts.builder()
                .setSubject("testuser@example.com") 
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        UserDetailsImpl userDetails = UserDetailsImpl.builder().username("alice@gmail.com").build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        LocalDateTime localDateTime =LocalDateTime.now();
		Date sessionDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        sessionOne = new Session();
        sessionOne.setId(1L);
        sessionOne.setName("Morning Flow Yog");
        sessionOne.setDate(sessionDate);
        sessionOne.setDescription("A dynamic morning flow to energize your body and mind. Suitable for all levels.");
        
        
        sessionTwo = new Session();
        sessionTwo.setId(2L);
        sessionTwo.setName("Evening Relaxation Yoga");
        sessionTwo.setDate(sessionDate);
        sessionTwo.setDescription("A calming yoga session to relax your mind and body after a long day. Perfect for beginners.");
        
        sessionDto = new SessionDto();
        sessionDto.setName("Evening Relaxation Yoga");
        sessionDto.setDate(sessionDate); 
        sessionDto.setDescription("A calming yoga session to relax your mind and body after a long day. Perfect for beginners.");
        sessionDto.setTeacher_id(2L);
       
    }
    
    
   

    @Test
    public void testFindSessionByIdWithSuccess() throws Exception {
        

        when(sessionService.getById(1L)).thenReturn(sessionOne);
      

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/1")
                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk());
             
    }
    
    @Test
    public void testFindSessionByIdReturnNotFound() throws Exception {
        

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/0")
                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
             
    }
    
    @Test
    public void testFindSessionByIdReturnBadRequest() throws Exception {
        

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/invalidId")
                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());
             
    }

    @Test
    public void testFindAllSessionsWithSuccess() throws Exception {
        

        List<Session> sessions = List.of(sessionOne, sessionTwo);

        when(sessionService.findAll()).thenReturn(sessions);
 
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate_Success() throws Exception {
    	
        when(sessionService.create(sessionTwo)).thenReturn(sessionTwo);
 
        mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdate_Success() throws Exception {
 
        when(sessionService.update(2L, sessionTwo)).thenReturn(sessionTwo);
  

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testUpdateReturnBadRequestInvalidId() throws Exception {
 
        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/invalidId")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isBadRequest());
    }
    
    

    @Test
    public void testDeleteSessionWithSuccess() throws Exception {
    	
    	when(sessionService.getById(1L)).thenReturn(sessionOne);  
    	doNothing().when(sessionService).delete(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testDeleteSessionNotFound() throws Exception {
  
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/4")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testDeleteSessionBadRequest() throws Exception {
 
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/invaliId")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testParticipateToSession_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/1/participate/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testParticipateToSessionBadRequestInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/1/participate/invaliId")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNoLongerParticipate_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/1/participate/2")
                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }
    @Test
    public void testNoLongerParticipateBadRequestInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/1/participate/invaliId")
                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());
    }

}

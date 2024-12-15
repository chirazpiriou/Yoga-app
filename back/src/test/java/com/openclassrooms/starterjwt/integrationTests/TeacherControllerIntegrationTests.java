package com.openclassrooms.starterjwt.integrationTests;

import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIntegrationTests {
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    
    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherMapper teacherMapper;
    

    private Authentication authentication;
    private UserDetailsImpl userDetails;
    private String jwtToken;
    
    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    @Value("${oc.app.jwtExpirationMs}")
    private long jwtExpirationMs;
    
    private User user;
    private Teacher teacherOne;
    private Teacher teacherTwo;
    
    @BeforeEach
    void setUp() {
       
        //Test user creation
        user = new User();
        user.setId(1L);
        user.setEmail("alice@gmail.com");
        user.setPassword("chats147");
        
     // Generate a user-valid JWT with application properties
        jwtToken = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))  
                .signWith(SignatureAlgorithm.HS512, jwtSecret)  
                .compact();
        
        userDetails = UserDetailsImpl.builder().username("alice@gmail.com").build();
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        teacherOne = new Teacher();
        teacherOne.setId(1L);
        teacherOne.setFirstName("Alain");
        teacherOne.setLastName("lin");
        
        
        teacherTwo = new Teacher();
        teacherTwo.setId(1L);
        teacherTwo.setFirstName("Alain");
        teacherTwo.setLastName("lin");
        
        List<Teacher> teachers = List.of(teacherOne, teacherTwo);
    }
    
    
    @Test
    public void testTeacherFindByIdSucess() throws Exception {
    	
        
        when(teacherService.findById(1L)).thenReturn(teacherOne);
       
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    public void testTeacherFindByIdNotFound() throws Exception {
    
        
        when(teacherService.findById(1L)).thenReturn(null);
       
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    
    @Test
    public void testTeacherFindByIdBadRequest() throws Exception {
    
     
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/one")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    public void testFindAllTeachers() throws Exception {
    	List<Teacher> teachers = List.of(teacherOne, teacherTwo);
    	when(teacherService.findAll()).thenReturn(teachers);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}

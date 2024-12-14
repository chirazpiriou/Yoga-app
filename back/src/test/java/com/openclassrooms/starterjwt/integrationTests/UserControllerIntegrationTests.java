package com.openclassrooms.starterjwt.integrationTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import java.net.http.HttpHeaders;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("User controller Integration test")
public class UserControllerIntegrationTests {
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;
    
    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;
    

    private Authentication authentication;
    private UserDetailsImpl userDetails;
    private String jwtToken;
    
    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    @Value("${oc.app.jwtExpirationMs}")
    private long jwtExpirationMs;
    
    private User user;
    
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
    }
    
    @Test
    public void testUserFindByIdOKResponse() throws Exception {
    	
    	when(userService.findById(1L)).thenReturn(user);

        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk());
                


    }

    @Test
    public void testUserFindByIdNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/0")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testUserFindByIdBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/notanumber")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());

    }

 

    @Test
    public void testUserDeleteBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/notgoodId")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());

    }

    

}

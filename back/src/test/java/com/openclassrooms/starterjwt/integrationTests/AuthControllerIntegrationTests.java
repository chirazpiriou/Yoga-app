package com.openclassrooms.starterjwt.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Auth Controller Integration Tests")

public class AuthControllerIntegrationTests {
	
	@Autowired
    private MockMvc mockMvc;
	
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testLoginUser() throws Exception {
    	String uniqueEmail = UUID.randomUUID().toString() + "@mail.com";
    	
    	// Step 1: Register the user before attempting to log in
        SignupRequest signupRequestOne = new SignupRequest();
        signupRequestOne.setEmail(uniqueEmail);
        signupRequestOne.setPassword("robertoo124");
        signupRequestOne.setFirstName("Robertoo");
        signupRequestOne.setLastName("fellinii");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequestOne)))
                .andExpect(status().isOk());
        
     // Step 2: Test connection with the same logins
        LoginRequest loginRequestOne = new LoginRequest();
        loginRequestOne.setEmail(uniqueEmail);
        loginRequestOne.setPassword("robertoo124");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestOne)))
                .andExpect(status().isOk());
    }
    
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testRegisterUserEmailAlreadyTaken() throws Exception {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga@studio.com");
        signupRequest.setFirstName("aline");
        signupRequest.setLastName("Dupuis");
        signupRequest.setPassword("87jiji");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testLoginBadCredentials() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("isWrongPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

 
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testRegisterUserOk() throws Exception {

        SignupRequest signupRequest = new SignupRequest();
        // generate a new email with a randomUUID
        signupRequest.setEmail(UUID.randomUUID().toString() + "@mail.com");
        signupRequest.setFirstName("newUse");
        signupRequest.setLastName("newUse");
        signupRequest.setPassword("newUse");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk());
    }

}

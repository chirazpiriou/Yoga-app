package com.openclassrooms.starterjwt.integrationTests;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Auth Controller Integration Tests")

public class AuthControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoginUser() throws Exception {
        String requestBodyLogin = "{" +
                "\"email\": \"yoga@studio.com\"," +
                "\"password\": \"test!1234\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyLogin))
                .andExpect(status().isOk());
    } 

   

    @Test
    public void testRegisterUserEmailAlreadyTaken() throws Exception {
        String requestBodyRegister = "{" +
                "\"email\": \"yoga@studio.com\"," +
                "\"lastName\": \"Alain\"," +
                "\"firstName\": \"Duchamps\"," +
                "\"password\": \"ccdrtgtggtt\"" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyRegister))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginBadCredentials() throws Exception {
        String requestBodyLogin = "{" +
                "\"email\": \"yoga@studio.com\"," +
                "\"password\": \"invalidPassword\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyLogin))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testRegisterUserOk() throws Exception {
        String requestBodyRegister = "{" +
                "\"email\": \"NewUserRegister@gmail.com\"," +
                "\"lastName\": \"Pascal\"," +
                "\"firstName\": \"Duchamps\"," +
                "\"password\": \"ccggtt\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyRegister))
                .andExpect(status().isOk());
    }

}

package com.openclassrooms.starterjwt.integrationTests;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Session Controller Integration Tests")
public class SessionControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    private String jwtToken;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("yoga@studio.com");
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        jwtToken = jwtUtils.generateJwtToken(authentication);

    }

    @Test
    public void testFindSessionByIdWithSuccess() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Session1"));

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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    public void testCreate_Success() throws Exception {
        String requestBodyCreate = "{" +
            "\"name\": \"Session3\"," +
            "\"description\": \"Description3\"," +
            "\"teacher_id\": 1," +
            "\"date\": \"2025-01-04T00:00:00\"" +
        "}";


        mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyCreate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Session3"));
    }

    @Test
    public void testUpdate_Success() throws Exception {
        String requestBodyUpdate = "{" +
            "\"name\": \"SessionUpdated\"," +
            "\"description\": \"DescriptionUpdate\"," +
            "\"teacher_id\": 1," +
            "\"date\": \"2025-01-04T00:00:00\"" +
        "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyUpdate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("SessionUpdated"));
    }

    @Test
    public void testUpdateReturnBadRequestInvalidId() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/invalidId")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteSessionWithSuccess() throws Exception {

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
        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/2/participate/2")
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
    public void testNoLongerParticipateBadRequestInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/1/participate/invaliId")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());
    }

}

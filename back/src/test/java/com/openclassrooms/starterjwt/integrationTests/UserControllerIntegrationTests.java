package com.openclassrooms.starterjwt.integrationTests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.security.core.Authentication;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.HttpHeaders;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class UserControllerIntegrationTests {
  @Autowired
  MockMvc mockMvc;
  private String jwtToken;
  @Autowired
  private UserDetailsServiceImpl userDetailsService;
  private Authentication authentication;
  @Autowired
  private JwtUtils jwtUtils;

  @BeforeAll
  void setUp() throws Exception {
    UserDetails userDetails = userDetailsService.loadUserByUsername("yoga@studio.com");
    authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    jwtToken = jwtUtils.generateJwtToken(authentication);
  }

  @Test
  public void testUserFindByIdOKResponse() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.lastName").value("Admin"));
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
  void testUserDeleteIsOkResponse() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", "1")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void testUserDeleteNotFoundIdNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", "3")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testUserDeleteBadRequest() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", "invalidId")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testUserDeleteUnauthorizedBecauseUserNotMatching() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", "1")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "badToken"))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }
} 
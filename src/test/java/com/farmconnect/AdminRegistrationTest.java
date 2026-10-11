package com.farmconnect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.farmconnect.dto.AdminRegistrationRequest;
import com.farmconnect.entity.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminRegistrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Admin validation endpoint returns valid=true for correct code and false for incorrect")
    void testValidateAdminCodeEndpoint() throws Exception {
        mockMvc.perform(get("/api/auth/validate-admin-code")
                        .param("code", "FC-ADMIN-2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));

        mockMvc.perform(get("/api/auth/validate-admin-code")
                        .param("code", "WRONG-CODE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));
    }

    @Test
    @DisplayName("Admin registration succeeds when valid admin authorization code is provided")
    void testAdminRegistrationSuccess() throws Exception {
        AdminRegistrationRequest req = new AdminRegistrationRequest();
        req.setFullName("Super Administrator");
        req.setPhone("+91 97777 " + (System.currentTimeMillis() % 100000));
        req.setPassword("adminSecurePass123");
        req.setAdminCode("FC-ADMIN-2026");

        mockMvc.perform(post("/api/auth/register/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.fullName").value("Super Administrator"))
                .andExpect(jsonPath("$.message", containsString("registered successfully")));
    }

    @Test
    @DisplayName("Admin registration is rejected when invalid admin code is provided")
    void testAdminRegistrationFailsWithInvalidCode() throws Exception {
        AdminRegistrationRequest req = new AdminRegistrationRequest();
        req.setFullName("Unauthorized Attacker");
        req.setPhone("+91 96666 " + (System.currentTimeMillis() % 100000));
        req.setPassword("adminSecurePass123");
        req.setAdminCode("INVALID-HACK-KEY");

        mockMvc.perform(post("/api/auth/register/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Invalid Admin Authorization Code")));
    }

    @Test
    @DisplayName("Admin registration is rejected when phone number is already registered")
    void testAdminRegistrationFailsIfPhoneRegistered() throws Exception {
        AdminRegistrationRequest req = new AdminRegistrationRequest();
        req.setFullName("Second Admin");
        // Using existing seeded phone 9999900000
        req.setPhone("9999900000");
        req.setPassword("anotherPassword");
        req.setAdminCode("FC-ADMIN-2026");

        mockMvc.perform(post("/api/auth/register/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("already registered")));
    }
}

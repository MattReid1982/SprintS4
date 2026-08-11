package com.controller;

import com.model.User;
import com.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.AirportApiApplication;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = AirportApiApplication.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User("admin", "password123", "admin@airport.com");
        testUser.setId(1L);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(testUser));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[0].email").value("admin@airport.com"));
    }

    @Test
    public void testGetUserByIdFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUser() throws Exception {
        when(userService.saveUser(any(User.class))).thenReturn(testUser);

        String jsonPayload = """
                {
                    "username": "admin",
                    "passwordHash": "password123",
                    "email": "admin@airport.com"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}

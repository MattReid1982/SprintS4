package com.groupx.sprints4.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Passenger;
import com.service.PassengerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PassengerController.class)
class PassengerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerService passengerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Passenger passenger;

    @BeforeEach
    void setUp() {
        passenger = new Passenger("John", "Doe", "123-4567");
        passenger.setId(1L);
    }

    @Test
    void testGetAllPassengers() throws Exception {
        when(passengerService.getAllPassengers()).thenReturn(Arrays.asList(passenger));

        mockMvc.perform(get("/api/passengers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void testGetPassengerById() throws Exception {
        when(passengerService.getPassengerById(1L)).thenReturn(Optional.of(passenger));

        mockMvc.perform(get("/api/passengers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testGetPassengerByIdNotFound() throws Exception {
        when(passengerService.getPassengerById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/passengers/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePassenger() throws Exception {
        when(passengerService.createPassenger(any(Passenger.class))).thenReturn(passenger);

        mockMvc.perform(post("/api/passengers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passenger)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testUpdatePassenger() throws Exception {
        Passenger updatedPassenger = new Passenger("Jane", "Doe", "987-6543");
        updatedPassenger.setId(1L);

        when(passengerService.updatePassenger(eq(1L), any(Passenger.class))).thenReturn(updatedPassenger);

        mockMvc.perform(put("/api/passengers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPassenger)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    void testDeletePassenger() throws Exception {
        doNothing().when(passengerService).deletePassenger(1L);

        mockMvc.perform(delete("/api/passengers/1"))
                .andExpect(status().isNoContent());
    }
}

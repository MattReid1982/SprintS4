package com.controller;

import com.model.Flight;
import com.model.FlightStatus;
import com.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
public class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightService flightService;

    private Flight testFlight;

    @BeforeEach
    public void setUp() {
        testFlight = new Flight();
        testFlight.setId(1L);
        testFlight.setFlightNumber("AC101");
        testFlight.setStatus(FlightStatus.ON_TIME);
    }

    @Test
    public void testGetAllFlights() throws Exception {
        when(flightService.getAllFlights()).thenReturn(List.of(testFlight));

        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].flightNumber").value("AC101"));
    }

    @Test
    public void testGetFlightByIdFound() throws Exception {
        when(flightService.findFlightById(1L)).thenReturn(Optional.of(testFlight));

        mockMvc.perform(get("/api/flights/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("AC101"));
    }

    @Test
    public void testGetFlightByIdNotFound() throws Exception {
        when(flightService.findFlightById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/flights/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateFlight() throws Exception {
        when(flightService.createFlight(any(Flight.class))).thenReturn(testFlight);

        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"flightNumber\":\"AC101\",\"status\":\"ON_TIME\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flightNumber").value("AC101"));
    }

    @Test
    public void testUpdateFlight() throws Exception {
        when(flightService.updateFlight(eq(1L), any(Flight.class))).thenReturn(testFlight);

        mockMvc.perform(put("/api/flights/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"flightNumber\":\"AC101\",\"status\":\"ON_TIME\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("AC101"));
    }

    @Test
    public void testDeleteFlight() throws Exception {
        doNothing().when(flightService).deleteFlight(1L);

        mockMvc.perform(delete("/api/flights/1"))
                .andExpect(status().isNoContent());
    }
}

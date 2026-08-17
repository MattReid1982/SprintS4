package com.controller;

import com.AirportApiApplication;
import com.exception.ResourceNotFoundException;
import com.model.Flight;
import com.model.FlightStatus;
import com.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMvc tests for FlightController.
 * Verifies HTTP verbs (GET, POST, PUT, DELETE), JSON payloads,
 * and response statuses (200, 201, 400, 404).
 */
@WebMvcTest(FlightController.class)
@ContextConfiguration(classes = AirportApiApplication.class)
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
        testFlight.setDepartureTime(LocalDateTime.of(2026, 8, 20, 8, 0));
        testFlight.setArrivalTime(LocalDateTime.of(2026, 8, 20, 11, 30));
        testFlight.setStatus(FlightStatus.ON_TIME);
    }

    // ==================== GET ALL (PAGINATED) ====================

    /** Verify GET /api/flights returns 200 with paginated content and metadata. */
    @Test
    public void testGetAllFlights() throws Exception {
        Flight flight2 = new Flight();
        flight2.setId(2L);
        flight2.setFlightNumber("WS101");

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Flight> flightPage = new PageImpl<>(List.of(testFlight, flight2), pageable, 2);

        when(flightService.getAllFlights(any(Pageable.class))).thenReturn(flightPage);

        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].flightNumber").value("AC101"))
                .andExpect(jsonPath("$.content[1].flightNumber").value("WS101"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    /** Verify GET /api/flights returns 200 with empty content when no flights exist. */
    @Test
    public void testGetAllFlightsEmpty() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Flight> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(flightService.getAllFlights(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    /** Verify GET /api/flights?page=0&size=5&sort=flightNumber,desc applies custom pagination. */
    @Test
    public void testGetAllFlightsCustomPagination() throws Exception {
        Flight flight2 = new Flight();
        flight2.setId(2L);
        flight2.setFlightNumber("WS101");

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "flightNumber"));
        Page<Flight> flightPage = new PageImpl<>(List.of(flight2, testFlight), pageable, 2);

        when(flightService.getAllFlights(any(Pageable.class))).thenReturn(flightPage);

        mockMvc.perform(get("/api/flights")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "flightNumber,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].flightNumber").value("WS101"))
                .andExpect(jsonPath("$.content[1].flightNumber").value("AC101"))
                .andExpect(jsonPath("$.size").value(5));
    }

    // ==================== GET BY ID ====================

    /** Verify GET /api/flights/{id} returns 200 with the flight JSON when found. */
    @Test
    public void testGetFlightByIdFound() throws Exception {
        when(flightService.findFlightById(1L)).thenReturn(Optional.of(testFlight));

        mockMvc.perform(get("/api/flights/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("AC101"))
                .andExpect(jsonPath("$.status").value("ON_TIME"));
    }

    /** Verify GET /api/flights/{id} returns 404 when the flight does not exist. */
    @Test
    public void testGetFlightByIdNotFound() throws Exception {
        when(flightService.findFlightById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/flights/99"))
                .andExpect(status().isNotFound());
    }

    // ==================== CREATE (POST) ====================

    /** Verify POST /api/flights returns 200 with the created flight in the JSON body. */
    @Test
    public void testCreateFlight() throws Exception {
        when(flightService.createFlight(any(Flight.class))).thenReturn(testFlight);

        String jsonPayload = """
                {
                    "flightNumber": "AC101",
                    "departureTime": "2026-08-20T08:00:00",
                    "arrivalTime": "2026-08-20T11:30:00",
                    "status": "ON_TIME"
                }
                """;

        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("AC101"))
                .andExpect(jsonPath("$.status").value("ON_TIME"));
    }

    /** Verify POST /api/flights returns 400 when the JSON payload is malformed. */
    @Test
    public void testCreateFlightBadRequest() throws Exception {
        String badPayload = "{ invalid json }";

        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badPayload))
                .andExpect(status().isBadRequest());
    }

    // ==================== UPDATE (PUT) ====================

    /** Verify PUT /api/flights/{id} returns 200 with the updated flight. */
    @Test
    public void testUpdateFlightSuccess() throws Exception {
        Flight updatedFlight = new Flight();
        updatedFlight.setId(1L);
        updatedFlight.setFlightNumber("AC999");
        updatedFlight.setStatus(FlightStatus.DELAYED);

        when(flightService.updateFlight(eq(1L), any(Flight.class)))
                .thenReturn(updatedFlight);

        String jsonPayload = """
                {
                    "flightNumber": "AC999",
                    "status": "DELAYED"
                }
                """;

        mockMvc.perform(put("/api/flights/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("AC999"))
                .andExpect(jsonPath("$.status").value("DELAYED"));
    }

    /** Verify PUT /api/flights/{id} returns 404 when the flight does not exist. */
    @Test
    public void testUpdateFlightNotFound() throws Exception {
        when(flightService.updateFlight(eq(99L), any(Flight.class)))
                .thenThrow(new ResourceNotFoundException("Flight not found with id: 99"));

        String jsonPayload = """
                {
                    "flightNumber": "AC999"
                }
                """;

        mockMvc.perform(put("/api/flights/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isNotFound());
    }

    /** Verify PUT /api/flights/{id} returns 400 when the JSON payload is malformed. */
    @Test
    public void testUpdateFlightBadRequest() throws Exception {
        String badPayload = "{ not valid }";

        mockMvc.perform(put("/api/flights/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badPayload))
                .andExpect(status().isBadRequest());
    }

    // ==================== DELETE ====================

    /** Verify DELETE /api/flights/{id} returns 204 No Content on success. */
    @Test
    public void testDeleteFlightSuccess() throws Exception {
        doNothing().when(flightService).deleteFlight(1L);

        mockMvc.perform(delete("/api/flights/1"))
                .andExpect(status().isNoContent());
    }

    /** Verify DELETE /api/flights/{id} returns 404 when the flight does not exist. */
    @Test
    public void testDeleteFlightNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Flight not found with id: 99"))
                .when(flightService).deleteFlight(99L);

        mockMvc.perform(delete("/api/flights/99"))
                .andExpect(status().isNotFound());
    }
}

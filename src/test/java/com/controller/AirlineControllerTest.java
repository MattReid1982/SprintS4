package com.controller;

import com.AirportApiApplication;
import com.exception.ResourceNotFoundException;
import com.model.Airline;
import com.service.AirlineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMvc tests for AirlineController.
 * Verifies HTTP verbs (GET, POST, PUT, DELETE), JSON payloads,
 * and response statuses (200, 201, 400, 404).
 */
@WebMvcTest(AirlineController.class)
@ContextConfiguration(classes = AirportApiApplication.class)
public class AirlineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AirlineService airlineService;

    private Airline testAirline;

    @BeforeEach
    public void setUp() {
        testAirline = new Airline("Air Canada", "AC");
        testAirline.setId(1L);
    }

    // ==================== GET ALL ====================

    /** Verify GET /api/airlines returns 200 with a JSON array of airlines. */
    @Test
    public void testGetAllAirlines() throws Exception {
        when(airlineService.getAllAirlines()).thenReturn(List.of(testAirline));

        mockMvc.perform(get("/api/airlines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Air Canada"))
                .andExpect(jsonPath("$[0].code").value("AC"));
    }

    /** Verify GET /api/airlines returns 200 with an empty array when no airlines exist. */
    @Test
    public void testGetAllAirlinesEmpty() throws Exception {
        when(airlineService.getAllAirlines()).thenReturn(List.of());

        mockMvc.perform(get("/api/airlines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ==================== GET BY ID ====================

    /** Verify GET /api/airlines/{id} returns 200 when the airline exists. */
    @Test
    public void testGetAirlineByIdFound() throws Exception {
        when(airlineService.getAirlineById(1L)).thenReturn(Optional.of(testAirline));

        mockMvc.perform(get("/api/airlines/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Air Canada"))
                .andExpect(jsonPath("$.code").value("AC"));
    }

    /** Verify GET /api/airlines/{id} returns 404 when the airline does not exist. */
    @Test
    public void testGetAirlineByIdNotFound() throws Exception {
        when(airlineService.getAirlineById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/airlines/99"))
                .andExpect(status().isNotFound());
    }

    // ==================== GET BY CODE ====================

    /** Verify GET /api/airlines/code/{code} returns 200 when the code matches. */
    @Test
    public void testGetAirlineByCodeFound() throws Exception {
        when(airlineService.getAirlineByCode("AC")).thenReturn(Optional.of(testAirline));

        mockMvc.perform(get("/api/airlines/code/AC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Air Canada"));
    }

    /** Verify GET /api/airlines/code/{code} returns 404 for an unknown code. */
    @Test
    public void testGetAirlineByCodeNotFound() throws Exception {
        when(airlineService.getAirlineByCode("XX")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/airlines/code/XX"))
                .andExpect(status().isNotFound());
    }

    // ==================== CREATE (POST) ====================

    /** Verify POST /api/airlines returns 200 with the created airline in the JSON body. */
    @Test
    public void testCreateAirline() throws Exception {
        when(airlineService.saveAirline(any(Airline.class))).thenReturn(testAirline);

        String jsonPayload = """
                {
                    "name": "Air Canada",
                    "code": "AC"
                }
                """;

        mockMvc.perform(post("/api/airlines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Air Canada"))
                .andExpect(jsonPath("$.code").value("AC"));
    }

    /** Verify POST /api/airlines returns 400 when the JSON payload is malformed. */
    @Test
    public void testCreateAirlineBadRequest() throws Exception {
        String badPayload = "{ invalid json }";

        mockMvc.perform(post("/api/airlines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badPayload))
                .andExpect(status().isBadRequest());
    }

    // ==================== UPDATE (PUT) ====================

    /** Verify PUT /api/airlines/{id} returns 200 with the updated airline. */
    @Test
    public void testUpdateAirlineSuccess() throws Exception {
        Airline updatedAirline = new Airline("WestJet", "WS");
        updatedAirline.setId(1L);

        when(airlineService.updateAirline(eq(1L), any(Airline.class)))
                .thenReturn(updatedAirline);

        String jsonPayload = """
                {
                    "name": "WestJet",
                    "code": "WS"
                }
                """;

        mockMvc.perform(put("/api/airlines/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("WestJet"))
                .andExpect(jsonPath("$.code").value("WS"));
    }

    /** Verify PUT /api/airlines/{id} returns 404 when the airline does not exist. */
    @Test
    public void testUpdateAirlineNotFound() throws Exception {
        when(airlineService.updateAirline(eq(99L), any(Airline.class)))
                .thenThrow(new ResourceNotFoundException("Airline not found with id: 99"));

        String jsonPayload = """
                {
                    "name": "WestJet",
                    "code": "WS"
                }
                """;

        mockMvc.perform(put("/api/airlines/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isNotFound());
    }

    /** Verify PUT /api/airlines/{id} returns 400 when the JSON payload is malformed. */
    @Test
    public void testUpdateAirlineBadRequest() throws Exception {
        String badPayload = "{ not valid }";

        mockMvc.perform(put("/api/airlines/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badPayload))
                .andExpect(status().isBadRequest());
    }

    // ==================== DELETE ====================

    /** Verify DELETE /api/airlines/{id} returns 204 No Content on success. */
    @Test
    public void testDeleteAirlineSuccess() throws Exception {
        doNothing().when(airlineService).deleteAirline(1L);

        mockMvc.perform(delete("/api/airlines/1"))
                .andExpect(status().isNoContent());
    }

    /** Verify DELETE /api/airlines/{id} returns 404 when the airline does not exist. */
    @Test
    public void testDeleteAirlineNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Airline not found with id: 99"))
                .when(airlineService).deleteAirline(99L);

        mockMvc.perform(delete("/api/airlines/99"))
                .andExpect(status().isNotFound());
    }
}

package com.controller;

import com.AirportApiApplication;
import com.model.Airport;
import com.model.Gate;
import com.service.GateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import com.exception.ResourceNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GateController.class)
@ContextConfiguration(classes = AirportApiApplication.class)
public class GateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GateService gateService;

    private Gate testGate;
    private Airport testAirport;

    @BeforeEach
    public void setUp() {
        testAirport = new Airport("Toronto Pearson International Airport", "YYZ");
        testAirport.setId(10L);

        testGate = new Gate("A1", "Terminal 1", testAirport);
        testGate.setId(1L);
    }

    @Test
    public void testGetAllGates() throws Exception {
        when(gateService.getAllGates()).thenReturn(List.of(testGate));

        mockMvc.perform(get("/api/gates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gateNumber").value("A1"))
                .andExpect(jsonPath("$[0].terminal").value("Terminal 1"));
    }

    @Test
    public void testGetAllGatesWithAirportIdFilter() throws Exception {
        when(gateService.getGatesByAirport(10L)).thenReturn(List.of(testGate));

        mockMvc.perform(get("/api/gates").param("airportId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gateNumber").value("A1"));
    }

    @Test
    public void testGetGateById() throws Exception {
        when(gateService.getGate(1L)).thenReturn(testGate);

        mockMvc.perform(get("/api/gates/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gateNumber").value("A1"))
                .andExpect(jsonPath("$.terminal").value("Terminal 1"));
    }

    /** Verify GET /api/gates/{id} returns 404 when the gate does not exist. */
    @Test
    public void testGetGateByIdNotFound() throws Exception {
        when(gateService.getGate(99L))
                .thenThrow(new ResourceNotFoundException("Gate not found with id: 99"));

        mockMvc.perform(get("/api/gates/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetGatesByAirportEndpoint() throws Exception {
        when(gateService.getGatesByAirport(10L)).thenReturn(List.of(testGate));

        mockMvc.perform(get("/api/gates/airport/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gateNumber").value("A1"));
    }

    @Test
    public void testCreateGate() throws Exception {
        when(gateService.saveGate(any(Gate.class))).thenReturn(testGate);

        String jsonPayload = """
                {
                    "gateNumber": "A1",
                    "terminal": "Terminal 1"
                }
                """;

        mockMvc.perform(post("/api/gates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gateNumber").value("A1"));
    }

    /** Verify POST /api/gates returns 400 when the JSON payload is malformed. */
    @Test
    public void testCreateGateBadRequest() throws Exception {
        String badPayload = "{ invalid json }";

        mockMvc.perform(post("/api/gates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateGate() throws Exception {
        when(gateService.updateGate(eq(1L), any(Gate.class))).thenReturn(testGate);

        String jsonPayload = """
                {
                    "gateNumber": "A1",
                    "terminal": "Terminal 1"
                }
                """;

        mockMvc.perform(put("/api/gates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gateNumber").value("A1"));
    }

    /** Verify PUT /api/gates/{id} returns 404 when the gate does not exist. */
    @Test
    public void testUpdateGateNotFound() throws Exception {
        when(gateService.updateGate(eq(99L), any(Gate.class)))
                .thenThrow(new ResourceNotFoundException("Gate not found with id: 99"));

        String jsonPayload = """
                {
                    "gateNumber": "A1",
                    "terminal": "Terminal 1"
                }
                """;

        mockMvc.perform(put("/api/gates/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isNotFound());
    }

    /** Verify PUT /api/gates/{id} returns 400 when the JSON payload is malformed. */
    @Test
    public void testUpdateGateBadRequest() throws Exception {
        String badPayload = "{ not valid }";

        mockMvc.perform(put("/api/gates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteGate() throws Exception {
        doNothing().when(gateService).deleteGate(1L);

        mockMvc.perform(delete("/api/gates/1"))
                .andExpect(status().isNoContent());
    }

    /** Verify DELETE /api/gates/{id} returns 404 when the gate does not exist. */
    @Test
    public void testDeleteGateNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Gate not found with id: 99"))
                .when(gateService).deleteGate(99L);

        mockMvc.perform(delete("/api/gates/99"))
                .andExpect(status().isNotFound());
    }
}

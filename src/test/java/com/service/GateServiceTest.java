package com.service;

import com.exception.ResourceNotFoundException;
import com.model.Airport;
import com.model.Gate;
import com.repo.GateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GateServiceTest {

    @Mock
    private GateRepository gateRepository;

    @InjectMocks
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
    public void testGetAllGates() {
        when(gateRepository.findAll()).thenReturn(List.of(testGate));

        List<Gate> gates = gateService.getAllGates();
        assertEquals(1, gates.size());
        assertEquals("A1", gates.get(0).getGateNumber());
        assertEquals("Terminal 1", gates.get(0).getTerminal());
        verify(gateRepository, times(1)).findAll();
    }

    @Test
    public void testGetGateByIdFound() {
        when(gateRepository.findById(1L)).thenReturn(Optional.of(testGate));

        Gate found = gateService.getGate(1L);
        assertNotNull(found);
        assertEquals("A1", found.getGateNumber());
    }

    @Test
    public void testGetGateByIdNotFound() {
        when(gateRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gateService.getGate(99L));
    }

    @Test
    public void testGetGatesByAirport() {
        when(gateRepository.findByAirportId(10L)).thenReturn(List.of(testGate));

        List<Gate> gates = gateService.getGatesByAirport(10L);
        assertEquals(1, gates.size());
        assertEquals("A1", gates.get(0).getGateNumber());
        verify(gateRepository, times(1)).findByAirportId(10L);
    }

    @Test
    public void testSaveGate() {
        when(gateRepository.save(testGate)).thenReturn(testGate);

        Gate saved = gateService.saveGate(testGate);
        assertNotNull(saved);
        assertEquals("A1", saved.getGateNumber());
        verify(gateRepository, times(1)).save(testGate);
    }

    @Test
    public void testUpdateGate() {
        Gate updatedDetails = new Gate("A2", "Terminal 2");
        when(gateRepository.findById(1L)).thenReturn(Optional.of(testGate));
        when(gateRepository.save(any(Gate.class))).thenReturn(testGate);

        Gate updated = gateService.updateGate(1L, updatedDetails);
        assertNotNull(updated);
        assertEquals("A2", testGate.getGateNumber());
        assertEquals("Terminal 2", testGate.getTerminal());
    }

    @Test
    public void testDeleteGateSuccess() {
        when(gateRepository.existsById(1L)).thenReturn(true);
        doNothing().when(gateRepository).deleteById(1L);

        gateService.deleteGate(1L);
        verify(gateRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteGateNotFound() {
        when(gateRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> gateService.deleteGate(99L));
    }
}

package com.service;

import com.exception.ResourceNotFoundException;
import com.model.Airline;
import com.repo.AirlineRepository;
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
public class AirlineServiceTest {

    @Mock
    private AirlineRepository airlineRepository;

    @InjectMocks
    private AirlineService airlineService;

    private Airline testAirline;

    @BeforeEach
    public void setUp() {
        testAirline = new Airline("Air Canada", "AC");
        testAirline.setId(1L);
    }

    @Test
    public void testGetAllAirlines() {
        when(airlineRepository.findAll()).thenReturn(List.of(testAirline));

        List<Airline> airlines = airlineService.getAllAirlines();
        assertEquals(1, airlines.size());
        assertEquals("Air Canada", airlines.get(0).getName());
        verify(airlineRepository, times(1)).findAll();
    }

    @Test
    public void testGetAirlineByIdFound() {
        when(airlineRepository.findById(1L)).thenReturn(Optional.of(testAirline));

        Optional<Airline> found = airlineService.getAirlineById(1L);
        assertTrue(found.isPresent());
        assertEquals("AC", found.get().getCode());
    }

    @Test
    public void testGetAirlineByCode() {
        when(airlineRepository.findByCode("AC")).thenReturn(Optional.of(testAirline));

        Optional<Airline> found = airlineService.getAirlineByCode("AC");
        assertTrue(found.isPresent());
        assertEquals("Air Canada", found.get().getName());
    }

    @Test
    public void testSaveAirline() {
        when(airlineRepository.save(testAirline)).thenReturn(testAirline);

        Airline saved = airlineService.saveAirline(testAirline);
        assertNotNull(saved);
        assertEquals("AC", saved.getCode());
    }

    @Test
    public void testUpdateAirlineSuccess() {
        Airline updateInfo = new Airline("WestJet", "WS");
        when(airlineRepository.findById(1L)).thenReturn(Optional.of(testAirline));
        when(airlineRepository.save(any(Airline.class))).thenReturn(testAirline);

        Airline updated = airlineService.updateAirline(1L, updateInfo);
        assertNotNull(updated);
        assertEquals("WestJet", testAirline.getName());
        assertEquals("WS", testAirline.getCode());
    }

    @Test
    public void testUpdateAirlineNotFound() {
        Airline updateInfo = new Airline("WestJet", "WS");
        when(airlineRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> airlineService.updateAirline(99L, updateInfo));
    }

    @Test
    public void testDeleteAirlineSuccess() {
        when(airlineRepository.existsById(1L)).thenReturn(true);
        doNothing().when(airlineRepository).deleteById(1L);

        airlineService.deleteAirline(1L);
        verify(airlineRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAirlineNotFound() {
        when(airlineRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> airlineService.deleteAirline(99L));
    }
}

package com.service;

import com.exception.ResourceNotFoundException;
import com.model.Airport;
import com.model.City;
import com.repo.AirportRepository;
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
public class AirportServiceTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private AirportService airportService;

    private Airport testAirport;
    private City testCity;

    @BeforeEach
    public void setUp() {
        testCity = new City("Toronto", "Ontario", 2930000);
        testCity.setId(10L);

        testAirport = new Airport("Toronto Pearson International Airport", "YYZ");
        testAirport.setId(1L);
        testAirport.setCity(testCity);
    }

    @Test
    public void testGetAllAirports() {
        when(airportRepository.findAll()).thenReturn(List.of(testAirport));

        List<Airport> airports = airportService.getAllAirports();
        assertEquals(1, airports.size());
        assertEquals("YYZ", airports.get(0).getAirportCode());
    }

    @Test
    public void testGetAirportFound() {
        when(airportRepository.findById(1L)).thenReturn(Optional.of(testAirport));

        Airport found = airportService.getAirport(1L);
        assertNotNull(found);
        assertEquals("YYZ", found.getAirportCode());
    }

    @Test
    public void testGetAirportNotFound() {
        when(airportRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> airportService.getAirport(99L));
    }

    @Test
    public void testSaveAirport() {
        when(airportRepository.save(testAirport)).thenReturn(testAirport);

        Airport saved = airportService.saveAirport(testAirport);
        assertNotNull(saved);
        assertEquals("YYZ", saved.getAirportCode());
    }

    @Test
    public void testUpdateAirportSuccess() {
        Airport updatedDetails = new Airport("Billy Bishop Airport", "YTZ");
        when(airportRepository.findById(1L)).thenReturn(Optional.of(testAirport));
        when(airportRepository.save(any(Airport.class))).thenReturn(testAirport);

        Airport updated = airportService.updateAirport(1L, updatedDetails);
        assertNotNull(updated);
        assertEquals("Billy Bishop Airport", testAirport.getName());
        assertEquals("YTZ", testAirport.getAirportCode());
    }

    @Test
    public void testUpdateAirportNotFound() {
        Airport updatedDetails = new Airport("Billy Bishop Airport", "YTZ");
        when(airportRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> airportService.updateAirport(99L, updatedDetails));
    }

    @Test
    public void testDeleteAirportSuccess() {
        when(airportRepository.existsById(1L)).thenReturn(true);
        doNothing().when(airportRepository).deleteById(1L);

        airportService.deleteAirport(1L);
        verify(airportRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAirportNotFound() {
        when(airportRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> airportService.deleteAirport(99L));
    }

    @Test
    public void testGetAirportByCity() {
        when(airportRepository.findByCityId(10L)).thenReturn(List.of(testAirport));

        List<Airport> airports = airportService.getAirportByCity(10L);
        assertEquals(1, airports.size());
        assertEquals("YYZ", airports.get(0).getAirportCode());
    }
}

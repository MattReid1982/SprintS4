package com.service;

import com.exception.ResourceNotFoundException;
import com.model.Airport;
import com.model.Flight;
import com.model.FlightStatus;
import com.model.Plane;
import com.repo.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    private Flight testFlight;
    private Airport testDepartureAirport;
    private Airport testArrivalAirport;
    private Plane testPlane;

    @BeforeEach
    public void setUp() {
        testDepartureAirport = new Airport("Toronto Pearson International Airport", "YYZ");
        testDepartureAirport.setId(1L);

        testArrivalAirport = new Airport("Vancouver International Airport", "YVR");
        testArrivalAirport.setId(2L);

        testPlane = new Plane();
        testPlane.setId(1L);
        testPlane.setType("Boeing 737");

        testFlight = new Flight();
        testFlight.setId(1L);
        testFlight.setFlightNumber("AC101");
        testFlight.setDepartureTime(LocalDateTime.of(2026, 8, 20, 8, 0));
        testFlight.setArrivalTime(LocalDateTime.of(2026, 8, 20, 11, 30));
        testFlight.setStatus(FlightStatus.ON_TIME);
        testFlight.setDepartureAirport(testDepartureAirport);
        testFlight.setArrivalAirport(testArrivalAirport);
        testFlight.setPlane(testPlane);
    }

    // ==================== READ (GET) ====================

    @Test
    public void testGetAllFlights() {
        Flight flight2 = new Flight();
        flight2.setId(2L);
        flight2.setFlightNumber("WS101");

        when(flightRepository.findAll()).thenReturn(List.of(testFlight, flight2));

        List<Flight> flights = flightService.getAllFlights();

        assertNotNull(flights);
        assertEquals(2, flights.size());
        assertEquals("AC101", flights.get(0).getFlightNumber());
        assertEquals("WS101", flights.get(1).getFlightNumber());
        verify(flightRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllFlightsEmpty() {
        when(flightRepository.findAll()).thenReturn(List.of());

        List<Flight> flights = flightService.getAllFlights();

        assertNotNull(flights);
        assertTrue(flights.isEmpty());
        verify(flightRepository, times(1)).findAll();
    }

    // ==================== PAGINATION ====================

    /** Verifies paginated retrieval returns correct page content and metadata. */
    @Test
    public void testGetAllFlightsPaginated() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Flight> flightPage = new PageImpl<>(List.of(testFlight), pageable, 1);

        when(flightRepository.findAll(pageable)).thenReturn(flightPage);

        Page<Flight> result = flightService.getAllFlights(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("AC101", result.getContent().get(0).getFlightNumber());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        verify(flightRepository, times(1)).findAll(pageable);
    }

    /** Verifies paginated retrieval with page size 5 and sorting descending by flightNumber. */
    @Test
    public void testGetAllFlightsPaginatedDescSort() {
        Flight flight2 = new Flight();
        flight2.setId(2L);
        flight2.setFlightNumber("WS101");

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "flightNumber"));
        Page<Flight> flightPage = new PageImpl<>(List.of(flight2, testFlight), pageable, 2);

        when(flightRepository.findAll(pageable)).thenReturn(flightPage);

        Page<Flight> result = flightService.getAllFlights(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("WS101", result.getContent().get(0).getFlightNumber());
        assertEquals("AC101", result.getContent().get(1).getFlightNumber());
        assertEquals(5, result.getSize());
        verify(flightRepository, times(1)).findAll(pageable);
    }

    /** Verifies paginated retrieval returns empty page when no flights exist. */
    @Test
    public void testGetAllFlightsPaginatedEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Flight> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(flightRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<Flight> result = flightService.getAllFlights(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(flightRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testGetFlightByIdFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(testFlight));

        Flight found = flightService.getFlightById(1L);

        assertNotNull(found);
        assertEquals("AC101", found.getFlightNumber());
        assertEquals(FlightStatus.ON_TIME, found.getStatus());
        assertEquals("YYZ", found.getDepartureAirport().getAirportCode());
        assertEquals("YVR", found.getArrivalAirport().getAirportCode());
        verify(flightRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetFlightByIdNotFound() {
        when(flightRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> flightService.getFlightById(99L));
        verify(flightRepository, times(1)).findById(99L);
    }

    @Test
    public void testFindFlightByIdOptional() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(testFlight));

        Optional<Flight> found = flightService.findFlightById(1L);

        assertTrue(found.isPresent());
        assertEquals("AC101", found.get().getFlightNumber());
    }

    // ==================== CREATE (POST) ====================

    @Test
    public void testCreateFlight() {
        when(flightRepository.save(any(Flight.class))).thenReturn(testFlight);

        Flight saved = flightService.createFlight(testFlight);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals("AC101", saved.getFlightNumber());
        assertEquals(FlightStatus.ON_TIME, saved.getStatus());
        verify(flightRepository, times(1)).save(testFlight);
    }

    @Test
    public void testCreateFlightWithAssignedId() {
        Flight newFlight = new Flight();
        newFlight.setFlightNumber("UA202");
        newFlight.setStatus(FlightStatus.DELAYED);

        Flight savedFlight = new Flight();
        savedFlight.setId(10L);
        savedFlight.setFlightNumber("UA202");
        savedFlight.setStatus(FlightStatus.DELAYED);

        when(flightRepository.save(any(Flight.class))).thenReturn(savedFlight);

        Flight result = flightService.createFlight(newFlight);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("UA202", result.getFlightNumber());
        verify(flightRepository, times(1)).save(newFlight);
    }

    // ==================== UPDATE (PUT) ====================

    @Test
    public void testUpdateFlightSuccess() {
        Flight updateInfo = new Flight();
        updateInfo.setFlightNumber("AC999");
        updateInfo.setStatus(FlightStatus.DELAYED);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(testFlight));
        when(flightRepository.save(any(Flight.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Flight updated = flightService.updateFlight(1L, updateInfo);

        assertNotNull(updated);
        assertEquals("AC999", testFlight.getFlightNumber());
        assertEquals(FlightStatus.DELAYED, testFlight.getStatus());
        verify(flightRepository, times(1)).findById(1L);
        verify(flightRepository, times(1)).save(testFlight);
    }

    @Test
    public void testUpdateFlightNotFound() {
        Flight updateInfo = new Flight();
        updateInfo.setFlightNumber("AC999");

        when(flightRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> flightService.updateFlight(99L, updateInfo));
        verify(flightRepository, times(1)).findById(99L);
        verify(flightRepository, never()).save(any(Flight.class));
    }

    // ==================== DELETE (DELETE) ====================

    @Test
    public void testDeleteFlightSuccess() {
        when(flightRepository.existsById(1L)).thenReturn(true);
        doNothing().when(flightRepository).deleteById(1L);

        flightService.deleteFlight(1L);

        verify(flightRepository, times(1)).existsById(1L);
        verify(flightRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteFlightNotFound() {
        when(flightRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> flightService.deleteFlight(99L));
        verify(flightRepository, times(1)).existsById(99L);
        verify(flightRepository, never()).deleteById(anyLong());
    }
}
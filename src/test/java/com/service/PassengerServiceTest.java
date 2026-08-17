package com.service;

import com.exception.ResourceNotFoundException;
import com.model.Airport;
import com.model.Passenger;
import com.model.Plane;
import com.repo.PassengerRepository;
import com.repo.PlaneRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PlaneRepository planeRepository;

    @InjectMocks
    private PassengerService passengerService;

    private Passenger testPassenger;
    private Plane testPlane;
    private Airport testAirport;

    @BeforeEach
    public void setUp() {
        testPassenger = new Passenger();
        testPassenger.setId(1L);
        testPassenger.setFirstName("John");
        testPassenger.setLastName("Doe");
        testPassenger.setPhoneNumber("709-555-0199");

        testAirport = new Airport("Toronto Pearson", "YYZ");
        testAirport.setId(10L);

        testPlane = new Plane("Air Canada", "Boeing 737", 180);
        testPlane.setId(20L);
        testPlane.setAirports(List.of(testAirport));
    }

    @Test
    public void testGetAllPassengers() {
        when(passengerRepository.findAll()).thenReturn(List.of(testPassenger));

        List<Passenger> passengers = passengerService.getAllPassengers();
        assertEquals(1, passengers.size());
        assertEquals("John", passengers.get(0).getFirstName());
    }

    // ==================== PAGINATION ====================

    /** Verifies paginated retrieval returns correct page content and metadata. */
    @Test
    public void testGetAllPassengersPaginated() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Passenger> passengerPage = new PageImpl<>(List.of(testPassenger), pageable, 1);

        when(passengerRepository.findAll(pageable)).thenReturn(passengerPage);

        Page<Passenger> result = passengerService.getAllPassengers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getFirstName());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        verify(passengerRepository, times(1)).findAll(pageable);
    }

    /** Verifies paginated retrieval with descending sort by lastName. */
    @Test
    public void testGetAllPassengersPaginatedDescSort() {
        Passenger passenger2 = new Passenger("Jane", "Smith", "709-555-0100");
        passenger2.setId(2L);

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "lastName"));
        Page<Passenger> passengerPage = new PageImpl<>(List.of(passenger2, testPassenger), pageable, 2);

        when(passengerRepository.findAll(pageable)).thenReturn(passengerPage);

        Page<Passenger> result = passengerService.getAllPassengers(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Smith", result.getContent().get(0).getLastName());
        assertEquals("Doe", result.getContent().get(1).getLastName());
        assertEquals(5, result.getSize());
        verify(passengerRepository, times(1)).findAll(pageable);
    }

    /** Verifies paginated retrieval returns empty page when no passengers exist. */
    @Test
    public void testGetAllPassengersPaginatedEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Passenger> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(passengerRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<Passenger> result = passengerService.getAllPassengers(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(passengerRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testGetPassengerByIdFound() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(testPassenger));

        Optional<Passenger> found = passengerService.getPassengerById(1L);
        assertTrue(found.isPresent());
        assertEquals("Doe", found.get().getLastName());
    }

    @Test
    public void testCreatePassenger() {
        when(passengerRepository.save(testPassenger)).thenReturn(testPassenger);

        Passenger created = passengerService.createPassenger(testPassenger);
        assertNotNull(created);
        assertEquals("John", created.getFirstName());
    }

    @Test
    public void testUpdatePassengerSuccess() {
        Passenger updatedDetails = new Passenger();
        updatedDetails.setFirstName("Jane");
        updatedDetails.setLastName("Smith");

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(testPassenger));
        when(passengerRepository.save(any(Passenger.class))).thenReturn(testPassenger);

        Passenger updated = passengerService.updatePassenger(1L, updatedDetails);
        assertNotNull(updated);
        assertEquals("Jane", testPassenger.getFirstName());
        assertEquals("Smith", testPassenger.getLastName());
        assertEquals("709-555-0199", testPassenger.getPhoneNumber()); // unchanged
    }

    @Test
    public void testUpdatePassengerNotFound() {
        Passenger updatedDetails = new Passenger();
        when(passengerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> passengerService.updatePassenger(99L, updatedDetails));
    }

    @Test
    public void testDeletePassengerSuccess() {
        when(passengerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(passengerRepository).deleteById(1L);

        passengerService.deletePassenger(1L);
        verify(passengerRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePassengerNotFound() {
        when(passengerRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> passengerService.deletePassenger(99L));
    }

    @Test
    public void testGetPlanesForPassenger() {
        when(planeRepository.findByPassengersId(1L)).thenReturn(List.of(testPlane));

        List<Plane> planes = passengerService.getPlanesForPassenger(1L);
        assertEquals(1, planes.size());
        assertEquals("Boeing 737", planes.get(0).getType());
    }

    @Test
    public void testGetAirportsUsedByPassenger() {
        when(planeRepository.findByPassengersId(1L)).thenReturn(List.of(testPlane));

        List<Airport> airports = passengerService.getAirportsUsedByPassenger(1L);
        assertEquals(1, airports.size());
        assertEquals("YYZ", airports.get(0).getAirportCode());
    }
}

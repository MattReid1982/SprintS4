package com.groupx.sprints4.service;

import com.model.Passenger;
import com.repo.PassengerRepository;
import com.service.PassengerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerService passengerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPassengers() {
        Passenger p1 = new Passenger("John", "Doe", "123");
        Passenger p2 = new Passenger("Jane", "Doe", "456");
        when(passengerRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Passenger> result = passengerService.getAllPassengers();
        assertEquals(2, result.size());
        verify(passengerRepository, times(1)).findAll();
    }

    @Test
    void testGetPassengerById() {
        Passenger p1 = new Passenger("John", "Doe", "123");
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(p1));

        Optional<Passenger> result = passengerService.getPassengerById(1L);
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void testCreatePassenger() {
        Passenger p1 = new Passenger("John", "Doe", "123");
        when(passengerRepository.save(any(Passenger.class))).thenReturn(p1);

        Passenger result = passengerService.createPassenger(p1);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testDeletePassenger() {
        doNothing().when(passengerRepository).deleteById(1L);
        passengerService.deletePassenger(1L);
        verify(passengerRepository, times(1)).deleteById(1L);
    }
}

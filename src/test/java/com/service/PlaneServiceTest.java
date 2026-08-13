package com.service;

import com.exception.ResourceNotFoundException;
import com.model.Airport;
import com.model.Plane;
import com.repo.PlaneRepository;
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
public class PlaneServiceTest {

    @Mock
    private PlaneRepository planeRepository;

    @InjectMocks
    private PlaneService planeService;

    private Plane testPlane;
    private Airport testAirport;

    @BeforeEach
    public void setUp() {
        testAirport = new Airport("Toronto Pearson", "YYZ");
        testAirport.setId(10L);

        testPlane = new Plane("Air Canada", "Boeing 787", 250);
        testPlane.setId(1L);
        testPlane.setAirports(List.of(testAirport));
    }

    @Test
    public void testGetAllPlanes() {
        when(planeRepository.findAll()).thenReturn(List.of(testPlane));

        List<Plane> planes = planeService.getAllPlanes();
        assertEquals(1, planes.size());
        assertEquals("Boeing 787", planes.get(0).getType());
    }

    @Test
    public void testGetPlaneByIDFound() {
        when(planeRepository.findById(1L)).thenReturn(Optional.of(testPlane));

        Plane found = planeService.getPlaneByID(1L);
        assertNotNull(found);
        assertEquals("Air Canada", found.getAirlineName());
    }

    @Test
    public void testGetPlaneByIDNotFound() {
        when(planeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> planeService.getPlaneByID(99L));
    }

    @Test
    public void testCreatePlane() {
        when(planeRepository.save(testPlane)).thenReturn(testPlane);

        Plane created = planeService.createPlane(testPlane);
        assertNotNull(created);
        assertEquals(250, created.getNumOfPassengers());
    }

    @Test
    public void testUpdatePlaneSuccess() {
        Plane updatedDetails = new Plane("Porter", "Embraer E195-E2", 132);
        when(planeRepository.findById(1L)).thenReturn(Optional.of(testPlane));
        when(planeRepository.save(any(Plane.class))).thenReturn(testPlane);

        Plane updated = planeService.updatePlane(1L, updatedDetails);
        assertNotNull(updated);
        assertEquals("Porter", testPlane.getAirlineName());
        assertEquals("Embraer E195-E2", testPlane.getType());
        assertEquals(132, testPlane.getNumOfPassengers());
    }

    @Test
    public void testUpdatePlaneNotFound() {
        Plane updatedDetails = new Plane("Porter", "Embraer E195-E2", 132);
        when(planeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> planeService.updatePlane(99L, updatedDetails));
    }

    @Test
    public void testDeletePlaneByIDSuccess() {
        when(planeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(planeRepository).deleteById(1L);

        planeService.deletePlaneByID(1L);
        verify(planeRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePlaneByIDNotFound() {
        when(planeRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> planeService.deletePlaneByID(99L));
    }

    @Test
    public void testGetAirportsForPlane() {
        when(planeRepository.findById(1L)).thenReturn(Optional.of(testPlane));

        List<Airport> airports = planeService.getAirportsForPlane(1L);
        assertEquals(1, airports.size());
        assertEquals("YYZ", airports.get(0).getAirportCode());
    }
}

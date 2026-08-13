package com.service;

import com.exception.ResourceNotFoundException;
import com.model.City;
import com.repo.CityRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    private City testCity;

    @BeforeEach
    public void setUp() {
        testCity = new City("St. John's", "NL", 114000);
        testCity.setId(1L);
    }

    @Test
    public void testGetAllCitiesPaginated() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<City> cityPage = new PageImpl<>(List.of(testCity));
        when(cityRepository.findAll(pageable)).thenReturn(cityPage);

        Page<City> result = cityService.getAllCities(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals("St. John's", result.getContent().get(0).getName());
    }

    @Test
    public void testGetCityFound() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(testCity));

        City found = cityService.getCity(1L);
        assertNotNull(found);
        assertEquals("St. John's", found.getName());
    }

    @Test
    public void testGetCityNotFound() {
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.getCity(99L));
    }

    @Test
    public void testSaveCity() {
        when(cityRepository.save(testCity)).thenReturn(testCity);

        City saved = cityService.saveCity(testCity);
        assertNotNull(saved);
        assertEquals("NL", saved.getProvince());
    }

    @Test
    public void testUpdateCitySuccess() {
        City updateDetails = new City("Halifax", "NS", 440000);
        when(cityRepository.findById(1L)).thenReturn(Optional.of(testCity));
        when(cityRepository.save(any(City.class))).thenReturn(testCity);

        City updated = cityService.updateCity(1L, updateDetails);
        assertNotNull(updated);
        assertEquals("Halifax", testCity.getName());
        assertEquals("NS", testCity.getProvince());
        assertEquals(440000, testCity.getPopulation());
    }

    @Test
    public void testUpdateCityNotFound() {
        City updateDetails = new City("Halifax", "NS", 440000);
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.updateCity(99L, updateDetails));
    }

    @Test
    public void testDeleteCitySuccess() {
        when(cityRepository.existsById(1L)).thenReturn(true);
        doNothing().when(cityRepository).deleteById(1L);

        cityService.deleteCity(1L);
        verify(cityRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteCityNotFound() {
        when(cityRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> cityService.deleteCity(99L));
    }
}

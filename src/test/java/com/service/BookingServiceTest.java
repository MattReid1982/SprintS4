package com.service;

import com.exception.ResourceNotFoundException;
import com.model.Booking;
import com.repo.BookingRepository;
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
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private com.repo.PassengerRepository passengerRepository;
    @Mock
    private com.repo.PlaneRepository planeRepository;
    @Mock
    private com.repo.AirlineRepository airlineRepository;
    @Mock
    private com.repo.AirportRepository airportRepository;
    @Mock
    private com.repo.GateRepository gateRepository;

    @InjectMocks
    private BookingService bookingService;

    private Booking testBooking;

    @BeforeEach
    public void setUp() {
        testBooking = new Booking();
        testBooking.setId(1L);
        testBooking.setFlightNumber("AC101");
        testBooking.setStatus("BOOKED");
        testBooking.setBookingReference("BK-AC101-12345678");
    }

    @Test
    public void testGetAllBookings() {
        when(bookingRepository.findAll()).thenReturn(List.of(testBooking));

        List<Booking> bookings = bookingService.getAllBookings();
        assertEquals(1, bookings.size());
        assertEquals("AC101", bookings.get(0).getFlightNumber());
    }

    @Test
    public void testGetBookingsFilter() {
        when(bookingRepository.findByFlightNumberAndGateIdAndStatus("AC101", 1L, "CHECKED_IN"))
                .thenReturn(List.of(testBooking));

        List<Booking> results = bookingService.getBookings("CHECKED_IN", "AC101", 1L);
        assertEquals(1, results.size());
        verify(bookingRepository, times(1)).findByFlightNumberAndGateIdAndStatus("AC101", 1L, "CHECKED_IN");
    }

    @Test
    public void testGetBookingByIdFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

        Optional<Booking> found = bookingService.getBookingById(1L);
        assertTrue(found.isPresent());
        assertEquals("AC101", found.get().getFlightNumber());
    }

    @Test
    public void testCreateBookingAutoReference() {
        Booking newBooking = new Booking();
        newBooking.setFlightNumber("WS202");
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking created = bookingService.createBooking(newBooking);
        assertNotNull(created);
        assertNotNull(created.getBookingReference());
        assertTrue(created.getBookingReference().startsWith("BK-WS202-"));
        assertEquals("BOOKED", created.getStatus());
    }

    @Test
    public void testUpdateBookingSuccess() {
        Booking updatedDetails = new Booking();
        updatedDetails.setFlightNumber("AC102");
        updatedDetails.setStatus("CANCELLED");

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking updated = bookingService.updateBooking(1L, updatedDetails);
        assertNotNull(updated);
        assertEquals("AC102", testBooking.getFlightNumber());
        assertEquals("CANCELLED", testBooking.getStatus());
    }

    @Test
    public void testUpdateBookingNotFound() {
        Booking updatedDetails = new Booking();
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.updateBooking(99L, updatedDetails));
    }

    @Test
    public void testCheckInSuccess() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking checkedIn = bookingService.checkIn(1L);
        assertNotNull(checkedIn);
        assertEquals("CHECKED_IN", checkedIn.getStatus());
        assertNotNull(checkedIn.getCheckInTime());
    }

    @Test
    public void testCheckInNotFound() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.checkIn(99L));
    }

    @Test
    public void testDeleteBookingSuccess() {
        when(bookingRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookingRepository).deleteById(1L);

        bookingService.deleteBooking(1L);
        verify(bookingRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteBookingNotFound() {
        when(bookingRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> bookingService.deleteBooking(99L));
    }
}

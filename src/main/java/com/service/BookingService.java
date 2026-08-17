package com.service;

import com.exception.ResourceNotFoundException;
import com.model.*;
import com.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for Booking business logic including check-in workflow and gate synchronization.
 */
@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final PlaneRepository planeRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
    private final GateRepository gateRepository;

    public BookingService(
            BookingRepository bookingRepository,
            PassengerRepository passengerRepository,
            PlaneRepository planeRepository,
            AirlineRepository airlineRepository,
            AirportRepository airportRepository,
            GateRepository gateRepository) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.planeRepository = planeRepository;
        this.airlineRepository = airlineRepository;
        this.airportRepository = airportRepository;
        this.gateRepository = gateRepository;
    }

    @Transactional(readOnly = true)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookings(String status, String flightNumber, Long gateId) {
        if (flightNumber != null && gateId != null && status != null) {
            return bookingRepository.findByFlightNumberAndGateIdAndStatus(flightNumber, gateId, status);
        } else if (flightNumber != null && gateId != null) {
            return bookingRepository.findByFlightNumberAndGateId(flightNumber, gateId);
        } else if (gateId != null && status != null) {
            return bookingRepository.findByGateIdAndStatus(gateId, status);
        } else if (flightNumber != null && status != null) {
            return bookingRepository.findByFlightNumberAndStatus(flightNumber, status);
        } else if (gateId != null) {
            return bookingRepository.findByGateId(gateId);
        } else if (flightNumber != null) {
            return bookingRepository.findByFlightNumber(flightNumber);
        } else if (status != null) {
            return bookingRepository.findByStatus(status);
        }
        return bookingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Booking> getBookingByReference(String reference) {
        return bookingRepository.findByBookingReference(reference);
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByFlightNumber(String flightNumber) {
        return bookingRepository.findByFlightNumber(flightNumber);
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByPassenger(Long passengerId) {
        return bookingRepository.findByPassengerId(passengerId);
    }

    /**
     * Resolves foreign entities (Passenger, Plane, Airline, Airports, Gate) to managed entities.
     */
    private void resolveRelationships(Booking booking) {
        if (booking.getPassenger() != null) {
            Long passengerId = booking.getPassenger().getId();
            if (passengerId != null && passengerId > 0) {
                booking.setPassenger(passengerRepository.findById(passengerId).orElse(null));
            } else {
                booking.setPassenger(null);
            }
        }
        if (booking.getPlane() != null) {
            Long planeId = booking.getPlane().getId() != null ? booking.getPlane().getId() : booking.getPlane().getID();
            if (planeId != null && planeId > 0) {
                booking.setPlane(planeRepository.findById(planeId).orElse(null));
            } else {
                booking.setPlane(null);
            }
        }
        if (booking.getAirline() != null) {
            Long airlineId = booking.getAirline().getId();
            if (airlineId != null && airlineId > 0) {
                booking.setAirline(airlineRepository.findById(airlineId).orElse(null));
            } else {
                booking.setAirline(null);
            }
        }
        if (booking.getOriginAirport() != null) {
            Long originId = booking.getOriginAirport().getId();
            if (originId != null && originId > 0) {
                booking.setOriginAirport(airportRepository.findById(originId).orElse(null));
            } else {
                booking.setOriginAirport(null);
            }
        }
        if (booking.getDestinationAirport() != null) {
            Long destId = booking.getDestinationAirport().getId();
            if (destId != null && destId > 0) {
                booking.setDestinationAirport(airportRepository.findById(destId).orElse(null));
            } else {
                booking.setDestinationAirport(null);
            }
        }
        if (booking.getGate() != null) {
            Long gateId = booking.getGate().getId();
            if (gateId != null && gateId > 0) {
                Gate gate = gateRepository.findById(gateId).orElse(null);
                booking.setGate(gate);
                // Sync gate current flight and status if gate was assigned
                if (gate != null && booking.getFlightNumber() != null && !booking.getFlightNumber().isBlank()) {
                    gate.setCurrentFlight(booking.getFlightNumber());
                    if ("AVAILABLE".equalsIgnoreCase(gate.getStatus()) || gate.getStatus() == null) {
                        gate.setStatus("OCCUPIED");
                    }
                    gateRepository.save(gate);
                }
            } else {
                booking.setGate(null);
            }
        }
    }

    /**
     * Creates a new booking. Auto-generates a unique booking reference if omitted.
     * Also sets check-in timestamp if created in CHECKED_IN status.
     *
     * @param booking booking payload
     * @return saved Booking entity
     */
    public Booking createBooking(Booking booking) {
        if (booking.getBookingReference() == null || booking.getBookingReference().isBlank()) {
            String flightTag = booking.getFlightNumber() != null ? booking.getFlightNumber().replaceAll("\\s+", "") : "FL";
            String uniqueShort = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            booking.setBookingReference("BK-" + flightTag + "-" + uniqueShort);
        }
        if (booking.getStatus() == null || booking.getStatus().isBlank()) {
            booking.setStatus("BOOKED");
        }
        if ("CHECKED_IN".equalsIgnoreCase(booking.getStatus()) && (booking.getCheckInTime() == null || booking.getCheckInTime().isBlank())) {
            booking.setCheckInTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }

        resolveRelationships(booking);
        return bookingRepository.save(booking);
    }

    /**
     * Updates an existing booking's attributes including gate, seat, baggage, and status.
     *
     * @param id      booking ID
     * @param updated updated booking payload
     * @return updated Booking entity
     */
    public Booking updateBooking(Long id, Booking updated) {
        return bookingRepository.findById(id).map(existing -> {
            resolveRelationships(updated);

            if (updated.getFlightNumber() != null) existing.setFlightNumber(updated.getFlightNumber());
            if (updated.getPassenger() != null) existing.setPassenger(updated.getPassenger());
            if (updated.getPlane() != null) existing.setPlane(updated.getPlane());
            if (updated.getAirline() != null) existing.setAirline(updated.getAirline());
            if (updated.getOriginAirport() != null) existing.setOriginAirport(updated.getOriginAirport());
            if (updated.getDestinationAirport() != null) existing.setDestinationAirport(updated.getDestinationAirport());
            if (updated.getGate() != null) existing.setGate(updated.getGate());
            if (updated.getDepartureTime() != null) existing.setDepartureTime(updated.getDepartureTime());
            if (updated.getArrivalTime() != null) existing.setArrivalTime(updated.getArrivalTime());
            if (updated.getSeatNumber() != null) existing.setSeatNumber(updated.getSeatNumber());
            existing.setBaggageCount(updated.getBaggageCount());
            if (updated.getStatus() != null) {
                existing.setStatus(updated.getStatus());
                if ("CHECKED_IN".equalsIgnoreCase(updated.getStatus()) && (existing.getCheckInTime() == null || existing.getCheckInTime().isBlank())) {
                    existing.setCheckInTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                }
            }
            if (updated.getCheckInTime() != null) existing.setCheckInTime(updated.getCheckInTime());
            return bookingRepository.save(existing);
        }).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    /**
     * Performs check-in for a booking: sets status to CHECKED_IN and records timestamp.
     *
     * @param id booking ID
     * @return updated booking
     */
    public Booking checkIn(Long id) {
        return bookingRepository.findById(id).map(booking -> {
            booking.setStatus("CHECKED_IN");
            booking.setCheckInTime(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }
}

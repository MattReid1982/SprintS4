package com.service;

import com.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.model.Airport;
import com.model.Plane;
import com.repo.PlaneRepository;
import java.util.List;

/**
 * Service class for managing {@link Plane} entities.
 * Provides business logic for managing aircraft, passenger associations, and airport routes.
 */
@Service
@Transactional(readOnly = true)
public class PlaneService {

    /** Repository for plane database operations. */
    @Autowired
    private PlaneRepository planeRepository;

    /**
     * Retrieves all planes stored in the database.
     *
     * @return list of all {@link Plane} entities
     */
    public List<Plane> getAllPlanes() {
        return planeRepository.findAll();
    }

    /**
     * Retrieves a plane by its unique numeric ID.
     *
     * @param ID plane ID
     * @return matching {@link Plane}
     * @throws ResourceNotFoundException if plane with given ID is not found
     */
    @SuppressWarnings("null")
    public Plane getPlaneByID(long ID) {
        return planeRepository.findById(ID)
                .orElseThrow(() -> new ResourceNotFoundException("Plane not found with id: " + ID));
    }

    /**
     * Alias method for getPlaneByID matching Java camelCase naming conventions.
     *
     * @param id plane ID
     * @return matching {@link Plane}
     * @throws ResourceNotFoundException if plane with given ID is not found
     */
    public Plane getPlaneById(Long id) {
        return getPlaneByID(id);
    }

    /**
     * Deletes a plane from the database by ID.
     *
     * @param ID plane ID to delete
     * @throws ResourceNotFoundException if plane with given ID is not found
     */
    @Transactional(readOnly = false)
    @SuppressWarnings("null")
    public void deletePlaneByID(long ID) {
        if (!planeRepository.existsById(ID)) {
            throw new ResourceNotFoundException("Plane not found with id: " + ID);
        }
        planeRepository.deleteById(ID);
    }

    /**
     * Alias method for deletePlaneByID matching Java camelCase naming conventions.
     *
     * @param id plane ID to delete
     * @throws ResourceNotFoundException if plane with given ID is not found
     */
    @Transactional(readOnly = false)
    public void deletePlaneById(Long id) {
        deletePlaneByID(id);
    }

    /**
     * Creates and saves a new plane entity in the database.
     *
     * @param newPlane plane to create
     * @return newly saved {@link Plane}
     */
    @Transactional(readOnly = false)
    @SuppressWarnings("null")
    public Plane createPlane(Plane newPlane) {
        return planeRepository.save(newPlane);
    }

    /**
     * Retrieves all planes associated with a specific passenger ID.
     *
     * @param passengerId passenger ID
     * @return list of {@link Plane} entities
     */
    @SuppressWarnings("null")
    public List<Plane> getPlanesByPassengerId(Long passengerId) {
        return planeRepository.findByPassengersId(passengerId);
    }

    /**
     * Retrieves all airports served by a specific plane.
     *
     * @param ID plane ID
     * @return list of {@link Airport} entities
     */
    public List<Airport> getAirportsForPlane(long ID) {
        Plane plane = getPlaneByID(ID);
        return plane.getAirports() != null ? plane.getAirports() : List.of();
    }

    /**
     * Updates an existing plane's attributes in the database.
     *
     * @param ID           plane ID to update
     * @param updatedPlane plane object with updated fields
     * @return updated {@link Plane}
     * @throws ResourceNotFoundException if plane with given ID is not found
     */
    @Transactional(readOnly = false)
    @SuppressWarnings("null")
    public Plane updatePlane(long ID, Plane updatedPlane) {
        Plane planeToUpdate = planeRepository.findById(ID)
                .orElseThrow(() -> new ResourceNotFoundException("Plane not found with id: " + ID));

        if (updatedPlane.getAirlineName() != null) {
            planeToUpdate.setAirlineName(updatedPlane.getAirlineName());
        }
        if (updatedPlane.getType() != null) {
            planeToUpdate.setType(updatedPlane.getType());
        }
        if (updatedPlane.getNumOfPassengers() > 0) {
            planeToUpdate.setNumOfPassengers(updatedPlane.getNumOfPassengers());
        }
        if (updatedPlane.getTailNumber() != null) {
            planeToUpdate.setTailNumber(updatedPlane.getTailNumber());
        }
        if (updatedPlane.getManufacturer() != null) {
            planeToUpdate.setManufacturer(updatedPlane.getManufacturer());
        }
        if (updatedPlane.getStatus() != null) {
            planeToUpdate.setStatus(updatedPlane.getStatus());
        }
        if (updatedPlane.getAirline() != null) {
            planeToUpdate.setAirline(updatedPlane.getAirline());
        }

        return planeRepository.save(planeToUpdate);
    }
}

package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.model.Airport;
import com.model.Plane;
import com.repo.PlaneRepository;
import java.util.List;
import java.util.Optional;

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
     * @return matching {@link Plane}, or null if not found
     */
    @SuppressWarnings("null")
    public Plane getPlaneByID(long ID) {
        Optional<Plane> PlaneOptional = planeRepository.findById(ID);
        return PlaneOptional.orElse(null);
    }

    /**
     * Deletes a plane from the database by ID.
     *
     * @param ID plane ID to delete
     */
    @Transactional(readOnly = false)
    @SuppressWarnings("null")
    public void deletePlaneByID(long ID) {
        planeRepository.deleteById(ID);
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
        if (plane == null) {
            return List.of();
        }
        return plane.getAirports();
    }

    /**
     * Updates an existing plane's attributes in the database.
     *
     * @param ID           plane ID to update
     * @param updatedPlane plane object with updated fields
     * @return updated {@link Plane}, or null if plane ID does not exist
     */
    @Transactional(readOnly = false)
    @SuppressWarnings("null")
    public Plane updatePlane(long ID, Plane updatedPlane) {
        Optional<Plane> planeToUpdateOptional = planeRepository.findById(ID);

        if (planeToUpdateOptional.isPresent()) {
            Plane planeToUpdate = planeToUpdateOptional.get();

            planeToUpdate.setID((int) updatedPlane.getID());
            planeToUpdate.setAirlineName(updatedPlane.getAirlineName());
            planeToUpdate.setType(updatedPlane.getType());
            planeToUpdate.setNumOfPassengers(updatedPlane.getNumOfPassengers());

            return planeRepository.save(planeToUpdate);
        }

        return null;
    }
}

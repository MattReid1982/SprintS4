package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.model.Airport;
import com.model.Plane;
import com.service.PlaneService;

import java.util.List;

/**
 * REST controller for managing plane endpoints.
 */
@RestController
@CrossOrigin
public class PlaneController {

    /** Service layer dependency for plane operations. */
    @Autowired
    private PlaneService planeService;

    /**
     * GET /planes : Retrieves a list of all planes.
     *
     * @return list of all planes
     */
    @GetMapping("/planes")
    public List<Plane> getAllPlanes() {
        return planeService.getAllPlanes();
    }

    /**
     * GET /planes/{ID} : Retrieves a plane by ID.
     *
     * @param ID plane ID
     * @return matching plane, or null if not found
     */
    @GetMapping("/planes/{ID}")
    public Plane getPlaneByID(@PathVariable long ID) {
        return planeService.getPlaneByID(ID);
    }

    /**
     * POST /planes : Creates a new plane.
     *
     * @param plane plane payload
     * @return newly created plane
     */
    @PostMapping("/planes")
    public Plane createPlane(@RequestBody Plane plane) {
        return planeService.createPlane(plane);
    }

    /**
     * PUT /planes/{ID} : Updates an existing plane.
     *
     * @param ID    plane ID
     * @param plane updated plane payload
     * @return 200 OK with updated plane
     */
    @PutMapping("/planes/{ID}")
    public ResponseEntity<Plane> updatePlane(@PathVariable Long ID, @RequestBody Plane plane) {
        return ResponseEntity.ok(planeService.updatePlane(ID, plane));
    }

    /**
     * DELETE /planes/{ID} : Deletes a plane by ID.
     *
     * @param ID plane ID
     */
    @DeleteMapping("/planes/{ID}")
    public void deletePlaneByID(@PathVariable Long ID) {
        planeService.deletePlaneByID(ID);
    }

    /**
     * GET /planes/{ID}/airports : Retrieves all airports served by a plane.
     *
     * @param ID plane ID
     * @return list of airports served by the plane
     */
    @GetMapping("/planes/{ID}/airports")
    public List<Airport> getAirportsForPlane(@PathVariable long ID) {
        return planeService.getAirportsForPlane(ID);
    }
}

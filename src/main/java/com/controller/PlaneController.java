package com.controller;

import com.model.Airport;
import com.model.Plane;
import com.service.PlaneService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing plane endpoints.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/planes")
public class PlaneController {

    private final PlaneService planeService;

    /**
     * Constructs the PlaneController.
     *
     * @param planeService plane service
     */
    public PlaneController(PlaneService planeService) {
        this.planeService = planeService;
    }

    /**
     * GET /api/planes
     * Retrieves all planes.
     */
    @GetMapping
    public List<Plane> getAllPlanes() {
        return planeService.getAllPlanes();
    }

    /**
     * GET /api/planes/{id}
     * Retrieves one plane by ID.
     */
    @GetMapping("/{id}")
    public Plane getPlaneById(@PathVariable Long id) {
        return planeService.getPlaneByID(id);
    }

    /**
     * POST /api/planes
     * Creates a new plane.
     */
    @PostMapping
    public Plane createPlane(@RequestBody Plane plane) {
        return planeService.createPlane(plane);
    }

    /**
     * PUT /api/planes/{id}
     * Updates an existing plane.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Plane> updatePlane(
            @PathVariable Long id,
            @RequestBody Plane plane) {

        return ResponseEntity.ok(
                planeService.updatePlane(id, plane)
        );
    }

    /**
     * DELETE /api/planes/{id}
     * Deletes a plane.
     */
    @DeleteMapping("/{id}")
    public void deletePlaneById(@PathVariable Long id) {
        planeService.deletePlaneByID(id);
    }

    /**
     * GET /api/planes/{id}/airports
     * Retrieves all airports served by a plane.
     */
    @GetMapping("/{id}/airports")
    public List<Airport> getAirportsForPlane(@PathVariable Long id) {
        return planeService.getAirportsForPlane(id);
    }
}
package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.model.Airport;
import com.model.Plane;
import com.service.PlaneService;

import java.util.List;


@RestController
@CrossOrigin
public class PlaneController {

    @Autowired
    private PlaneService planeService;


    @GetMapping("/planes")
    public List<Plane> getAllPlanes(){
        return planeService.getAllPlanes();
    }

    @GetMapping("/planes/{ID}")
    public Plane getPlaneByID(@PathVariable long ID){
        return planeService.getPlaneByID(ID);
    }

    @PostMapping("/planes")
    public Plane createPlane(@RequestBody Plane plane) {
        return planeService.createPlane(plane);
    }

    @PutMapping("/planes/{ID}")
    public ResponseEntity<Plane> updatePlane(@PathVariable Long ID, @RequestBody Plane plane) {
        return ResponseEntity.ok(planeService.updatePlane(ID, plane));
    }

    @DeleteMapping("/planes/{ID}")
    public void deletePlaneByID(@PathVariable Long ID) {
        planeService.deletePlaneByID(ID);
    }

    @GetMapping("/planes/{ID}/airports")
    public List<Airport> getAirportsForPlane(@PathVariable long ID) {
        return planeService.getAirportsForPlane(ID);
    }
}

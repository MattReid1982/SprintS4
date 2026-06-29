package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.model.Plane;
import com.repo.PlaneRepository;
import java.util.List;
import java.util.Optional;

@Service
public class PlaneService {
    @Autowired
    private PlaneRepository planeRepository;

    public List<Plane> getAllPlanes(){
        return(List<Plane>) planeRepository.findAll();
    }


    public Plane getPlaneByID(long ID){
        Optional<Plane> PlaneOptional = planeRepository.findById(ID);
        return PlaneOptional.orElse(null);
    }

    public void deletePlaneByID(long ID){
        planeRepository.deleteById(ID);
    }

    public Plane createPlane(Plane newPlane){
        return planeRepository.save(newPlane);
    }

    public Plane updatePlane(long ID, Plane updatedPlane){
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

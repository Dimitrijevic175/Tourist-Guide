package org.example.backend.services;

import org.example.backend.entities.Destination;
import org.example.backend.repositories.destination.DestinationRepository;

import javax.inject.Inject;
import java.util.List;

public class DestinationService {

    @Inject
    private DestinationRepository destinationRepository;

    public Long getDestinationIdByName(String name){
        return this.destinationRepository.getDestinationIdByName(name);
    }

    public List<Destination> allDestinations(){
        return this.destinationRepository.allDestinations();
    }

    public Destination addDestination(Destination destination) {
        try {
            return destinationRepository.addDestination(destination);
        } catch (Exception e) {
            throw new RuntimeException("Error adding destination", e);
        }
    }

    public Destination updateDestination(Destination destination) {
        return destinationRepository.updateDestination(destination);
    }

    public void deleteDestination(Long destinationID) {
            destinationRepository.deleteDestination(destinationID);
    }

    public Destination getDestinationById(Long destinationID) {
        return destinationRepository.getDestinationById(destinationID);
    }


}

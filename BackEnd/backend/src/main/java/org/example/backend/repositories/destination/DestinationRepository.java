package org.example.backend.repositories.destination;

import org.example.backend.entities.Destination;

import java.util.List;

public interface DestinationRepository {

    Long getDestinationIdByName(String destinationName);
    List<Destination> allDestinations();
    Destination addDestination(Destination destination);
    Destination updateDestination(Destination destination);
    void deleteDestination(Long destinationID);

    Destination getDestinationById(Long destinationID);

}

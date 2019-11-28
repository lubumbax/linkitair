package com.lubumbax.linkitair.flights.repository;

import com.lubumbax.linkitair.flights.model.Airport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AirportsRepository extends MongoRepository<Airport, String> {
}

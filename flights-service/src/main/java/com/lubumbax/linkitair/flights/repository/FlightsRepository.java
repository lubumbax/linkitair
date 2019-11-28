package com.lubumbax.linkitair.flights.repository;

import com.lubumbax.linkitair.flights.model.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FlightsRepository extends MongoRepository<Flight, String> {
    Flight findByNumber(String number);

    @Query("{'from.description': {$regex: ?0, $options: 'i' }})")
    List<Flight> findByFromDescriptionLike(String match);

    @Query("{'to.description': {$regex: ?0, $options: 'i' }})")
    List<Flight> findByToDescriptionLike(String match);
}

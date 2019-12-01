package com.lubumbax.linkitair.flights.repository;

import com.lubumbax.linkitair.flights.model.Flight;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FlightsRepository extends MongoRepository<Flight, String> {
    @Query("{'from.code': ?0, 'to.code': ?1}")
    List<Flight> findByFromCodeAndToCodeOrOrderByTimeAsc(String from, String to, Sort sort);

    @Query("{'from.description': {$regex: ?0, $options: 'i'}}")
    List<Flight> findByFromDescriptionLike(String match);

    @Query("{'to.description': {$regex: ?0, $options: 'i'}}")
    List<Flight> findByToDescriptionLike(String match);

    @Query("{'from.description': {$regex: ?0, $options: 'i'}, 'to.description': {$regex: ?1, $options: 'i'}}")
    List<Flight> findByFromToDescriptionLike(String fromMatch, String toMatch);
}

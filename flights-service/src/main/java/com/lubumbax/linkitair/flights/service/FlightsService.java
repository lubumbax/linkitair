package com.lubumbax.linkitair.flights.service;

import com.lubumbax.linkitair.flights.model.Flight;
import com.lubumbax.linkitair.flights.repository.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FlightsService {

    private FlightsRepository repository;

    public List<Flight> getAllFlights() {
        return repository.findAll();
    }

    public List<Flight> getFlightsWhereFromDescriptionMatches(String match) {
        return repository.findByFromDescriptionLike(match.toLowerCase());
    }

    public List<Flight> getFlightsWhereToDescriptionMatches(String match) {
        return repository.findByToDescriptionLike(match.toLowerCase());
    }

    public List<Flight.AirportData> getAllAirports() {
        List<Flight> flights = repository.findAll();

        return Stream.concat(
                flights.stream().map(Flight::getFrom),
                flights.stream().map(Flight::getTo)
        )
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Flight.AirportData> getAirportsWhereFromDescriptionMatches(String match) {
        return repository.findByFromDescriptionLike(match.toLowerCase())
                .stream()
                .map(Flight::getFrom)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Flight.AirportData> getAirportsWhereToDescriptionMatches(String match) {
        return repository.findByToDescriptionLike(match.toLowerCase())
                .stream()
                .map(Flight::getTo)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getAllAirportDescriptions() {
        List<Flight> flights = repository.findAll();

        return Stream.concat(
                flights.stream().map(Flight::getFrom),
                flights.stream().map(Flight::getTo)
        )
                .map(Flight.AirportData::getDescription)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getAirportDescriptionsWhereFromDescriptionMatches(String match) {
        return repository.findByFromDescriptionLike(match.toLowerCase())
                .stream()
                .map(Flight::getFrom)
                .map(Flight.AirportData::getDescription)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getAirportDescriptionsWhereToDescriptionMatches(String match) {
        return repository.findByToDescriptionLike(match.toLowerCase())
                .stream()
                .map(Flight::getTo)
                .map(Flight.AirportData::getDescription)
                .distinct()
                .collect(Collectors.toList());
    }

    @Autowired
    private void setFlightsRepository(FlightsRepository repository) {
        this.repository = repository;
    }
}

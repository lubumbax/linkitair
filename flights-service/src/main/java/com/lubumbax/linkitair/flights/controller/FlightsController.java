package com.lubumbax.linkitair.flights.controller;

import com.lubumbax.linkitair.flights.model.Flight;
import com.lubumbax.linkitair.flights.service.FlightsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightsController {
    @Autowired
    private FlightsService flightsService;

    @Operation(summary = "Retrieve flights from one airport to another")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returned list of flights found")})
    @GetMapping("/from/{from}/to/{to}")
    public List<Flight> findFrom(
            @Parameter(description="Airport of departure, by its airport code", example = "AMS")
            @PathVariable String from,
            @Parameter(description="Airport of arrival, by its airport code", example = "LHR")
            @PathVariable String to
    ) {
        return flightsService.getFlightsFromTo(from, to);
    }

    @GetMapping("/from/{airportCode}")
    public List<Flight> findFrom(@PathVariable String airportCode) {
        return flightsService.getFlightsWhereFromDescriptionMatches(airportCode);
    }

    @GetMapping("/to/{airportCode}")
    public List<Flight> findTo(@PathVariable String airportCode) {
        return flightsService.getFlightsWhereToDescriptionMatches(airportCode);
    }

    @Operation(summary = "Retrieve departure airports matching the departure airport of existing flights")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returned list of airports found")})
    @GetMapping("/airports/from/{fromMatch}")
    public List<Flight.AirportData> findAirportsFrom(
            @PathVariable String fromMatch
    ) {
        return flightsService.getAirportsFromWhereFromDescriptionMatches(fromMatch);
    }

    @Operation(summary = "Retrieve arrival airports matching the departure and arrival airport of existing flights")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Returned list of airports found")})
    @GetMapping("/airports/from/{from}/to/{toMatch}")
    public List<Flight.AirportData> findAirportsTo(
            @PathVariable String from,
            @PathVariable String toMatch
    ) {
        return flightsService.getAirportsToWhereFromAndToDescriptionMatches(from, toMatch);
    }
}

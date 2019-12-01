package com.lubumbax.linkitair.flights.controller;

import com.lubumbax.linkitair.flights.model.Flight;
import com.lubumbax.linkitair.flights.service.FlightsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flights")
public class FlightsController {
    @Autowired
    private FlightsService flightsService;

//    @GetMapping
//    @ApiOperation(
//            value = "Retrieve flights",
//            notes = "Without parameters it retrieves all available flights. Pass 'from' or 'to' parameters to filter"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Returned list of found flights"),
//    })
//    public List<Flight> find(
//            @ApiParam(value = "if specified, filter by departure airports")
//            @RequestParam(name="from", required = false) Optional<String> from,
//            @ApiParam(value = "if specified, filter by arrival airports")
//            @RequestParam(name="to", required = false) Optional<String> to
//    ) {
//        if (from.isPresent()) {
//            return flightsService.getFlightsWhereFromDescriptionMatches(from.get());
//        }
//        if (to.isPresent()) {
//            return flightsService.getFlightsWhereToDescriptionMatches(to.get());
//        }
//        return flightsService.getAllFlights();
//    }

    @ApiOperation(
            value = "Retrieve flights from one airport to another"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returned list of flights found"),
    })
    @GetMapping("/from/{from}/to/{to}")
    public List<Flight> findFrom(
            @ApiParam(value="Airport of departure, by its airport code", example = "AMS")
            @PathVariable String from,
            @ApiParam(value="Airport of arrival, by its airport code", example = "LHR")
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


    @GetMapping("/airports/from/{from}")
    public List<Flight.AirportData> findAirports(
            @PathVariable String from
    ) {
        return flightsService.getAirportsFromWhereFromDescriptionMatches(from);
    }

    @GetMapping("/airports/from/{from}/to/{to}")
    public List<Flight.AirportData> findAirports(
            @PathVariable String from,
            @PathVariable String to
    ) {
        return flightsService.getAirportsToWhereFromAndToDescriptionMatches(from, to);
    }

    /* ---- */

    @GetMapping("/airports")
    public List<Flight.AirportData> findAirports(
            @RequestParam(name="from", required = false) Optional<String> from,
            @RequestParam(name="to", required = false) Optional<String> to
    ) {
        if (from.isPresent()) {
            return flightsService.getAirportsFromWhereFromDescriptionMatches(from.get());
        }
        if (to.isPresent()) {
            return flightsService.getAirportsToWhereToDescriptionMatches(to.get());
        }
        return flightsService.getAllAirports();
    }

    @GetMapping("/airports/description")
    public List<String> findAirportDescriptions(
            @RequestParam(name="from", required = false) Optional<String> from,
            @RequestParam(name="to", required = false) Optional<String> to
    ) {
        if (from.isPresent()) {
            return flightsService.getAirportDescriptionsWhereFromDescriptionMatches(from.get());
        }
        if (to.isPresent()) {
            return flightsService.getAirportDescriptionsWhereToDescriptionMatches(to.get());
        }
        return flightsService.getAllAirportDescriptions();
    }
}

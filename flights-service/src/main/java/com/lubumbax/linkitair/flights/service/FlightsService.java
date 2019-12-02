package com.lubumbax.linkitair.flights.service;

import com.lubumbax.linkitair.flights.error.ParameterNotValidException;
import com.lubumbax.linkitair.flights.model.Flight;
import com.lubumbax.linkitair.flights.repository.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handle access to Flight and FlightAirport data.
 *
 * Flight and FlightAirport data is retrieved from the data layer by matching different conditions.
 * A Flight has a "from" and a "to" airport (of type FlightAirport).
 * A FlightAirport has a "code" and a "description".
 * The matches performed by this service from/to a given airport are run against either the "code" or the "description".
 *
 * For documentation purposes, suppose the following existing flights:
 *
 * - LK001: from @AMS, to @LHR
 * - LK002: from @LHR, to @AMS
 * - LK003: from @AMS, to @FRA
 * - LK004: from @FRA, to @LHR
 *
 * The airports are (@CODE: description):
 *
 * - @AMS: AMS - Schiphol (Amsterdam)
 * - @LHR: LHR - Heathrow (London)
 * - @FRA: FRA - Frankfurt am Main (Frankfurt)
 *
 */
@Service
public class FlightsService {

    private FlightsRepository repository;

    /**
     * Retrieves all available flights between two airports by their given airport code.
     * The given airport codes have to match fully with those of the existing airports.
     *
     * Examples:
     *
     *   getFlightsFromTo("AMS", "LHR") returns LK001
     *
     * @param from String representing the code of a departure airport (eg. "AMS")
     * @param to String representing the code of an arrival airport (eg. "FRA")
     * @return List of Flight meeting the match condition
     * @throws ParameterNotValidException if 'from' or 'to' contain one of /, \, (, ), { or }
     */
    public List<Flight> getFlightsFromTo(String from, String to) {
        validateParameter("from", from);
        validateParameter("to", to);

        Sort timeAsc = Sort.by(Sort.Direction.ASC, "time");
        return repository.findByFromCodeAndToCodeOrOrderByTimeAsc(from, to, timeAsc);
    }

    private static final Pattern BAD_FILTER_REGEX = Pattern.compile("[\\/\\\\(){}]");

    public void validateParameter(String name, String value) throws ParameterNotValidException {
        Matcher matcher = BAD_FILTER_REGEX.matcher(value);
        if (matcher.find()){
            throw new ParameterNotValidException(name, value);
        }
    }

    /**
     * Retrieves all available flights that departure from a given airport by a token of airport description.
     * The given airport is a token that happens to match with the description of one of the existing airports.
     * A match happens when the token matches at least partially with the code, name or city of one of the airports in
     * departures (those that are part of the "from" field of a flight).
     *
     * Examples:
     *
     *   getFlightsWhereFromDescriptionMatches("am")  returns LK001, LK003, LK004
     *   getFlightsWhereFromDescriptionMatches("ams") returns LK001, LK003
     *
     * @param match a token to be checked against code, name or city of existing departure airports.
     * @return List of Flight meeting the match condition
     * @throws ParameterNotValidException if 'match' contains one of /, \, (, ), { or }
     */
    public List<Flight> getFlightsWhereFromDescriptionMatches(String match) {
        validateParameter("match", match);
        return repository.findByFromDescriptionLike(match.toLowerCase());
    }

    /**
     * Retrieves all available flights that arrive to a given airport.
     * The given airport is a token that happens to match with the description of one of the existing airports.
     * A match happens when the token matches at least partially with the code, name or city of one of the airports in
     * arrivals (those that are part of the "to" field of a flight).
     *
     * Examples:
     *
     *   getFlightsWhereFromDescriptionMatches("am")  returns LK002, LK003
     *   getFlightsWhereFromDescriptionMatches("ams") returns LK002
     *
     * @param match a token to be checked against code, name or city of existing arrival airports.
     * @return List of Flight meeting the match condition
     * @throws ParameterNotValidException if 'match' contains one of /, \, (, ), { or }
     */
    public List<Flight> getFlightsWhereToDescriptionMatches(String match) {
        validateParameter("match", match);
        return repository.findByToDescriptionLike(match.toLowerCase());
    }

    /**
     * Retrieves all airports for which there are departure or arrival flights.*
     * @return List of unique/distinct AirportData
     */
    public List<Flight.AirportData> getAllAirports() {
        List<Flight> flights = repository.findAll();

        return Stream.concat(
                flights.stream().map(Flight::getFrom),
                flights.stream().map(Flight::getTo)
        )
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all airports whose code, name or city matches a given token and from which there are existing
     * departure flights.
     *
     * Examples:
     *
     *   getAirportsFromWhereFromDescriptionMatches("am")  returns @AMS, @FRA
     *   getAirportsFromWhereFromDescriptionMatches("ams") returns @AMS
     *
     * @param match a token to be checked against code, name or city of existing departure airports.
     * @return List of Flight meeting the match condition
     * @throws ParameterNotValidException if 'match' contains one of /, \, (, ), { or }
     */
    public List<Flight.AirportData> getAirportsFromWhereFromDescriptionMatches(String match) {
        validateParameter("match", match);
        return repository.findByFromDescriptionLike(match.toLowerCase())
                .stream()
                .map(Flight::getFrom)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all airports whose code, name or city matches a given token and
     * to which there are existing arrival flights.
     *
     * Examples:
     *
     *   getAirportsToWhereToDescriptionMatches("am")  returns @AMS, @FRA
     *   getAirportsToWhereToDescriptionMatches("ams") returns @AMS
     *
     * @param match a token to be checked against code, name or city of existing departure airports.
     * @return List of Flight meeting the match condition
     * @throws ParameterNotValidException if 'match' contains one of /, \, (, ), { or }
     */
    public List<Flight.AirportData> getAirportsToWhereToDescriptionMatches(String match) {
        validateParameter("match", match);
        return repository.findByToDescriptionLike(match.toLowerCase())
                .stream()
                .map(Flight::getTo)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all airports whose code, name or city matches a given token and
     * to which there are existing arrival flights.
     * The involved flights must also departure from the given aiport code.
     *
     * In other words, this helps to find arrival airport matches but only for flights with departures from
     * a specific airport.
     *
     * Examples:
     *
     *   getAirportsToWhereFromAndToDescriptionMatches("AMS", "am")  returns @FRA
     *   getAirportsToWhereFromAndToDescriptionMatches("AMS", "ams") returns empty list
     *
     * @param from String representing the code of a departure airport (eg. "AMS")
     * @param toMatch a token to be checked against code, name or city of existing arrival airports.
     * @return List of Flight meeting the match condition
     * @throws ParameterNotValidException if 'from' or 'toMatch' contain one of /, \, (, ), { or }
     */
    public List<Flight.AirportData> getAirportsToWhereFromAndToDescriptionMatches(String from, String toMatch) {
        validateParameter("from", from);
        validateParameter("toMatch", toMatch);
        return repository.findByFromToDescriptionLike(from, toMatch.toLowerCase())
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
        validateParameter("match", match);
        return repository.findByFromDescriptionLike(match.toLowerCase())
                .stream()
                .map(Flight::getFrom)
                .map(Flight.AirportData::getDescription)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getAirportDescriptionsWhereToDescriptionMatches(String match) {
        validateParameter("match", match);
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

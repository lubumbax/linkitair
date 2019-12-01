package com.lubumbax.linkitair.flights.service;

import com.lubumbax.linkitair.flights.test.matcher.CaseInsensitiveEquals;
import com.lubumbax.linkitair.flights.model.Airport;
import com.lubumbax.linkitair.flights.model.Flight;
import com.lubumbax.linkitair.flights.repository.FlightsRepository;
import com.lubumbax.linkitair.flights.test.factory.AirportFactory;
import com.lubumbax.linkitair.flights.test.factory.FlightFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;
import static com.lubumbax.linkitair.flights.test.factory.FlightFactory.buildAirportData;

@SpringBootTest
public class FlightsServiceTest {
    @Mock
    private FlightsRepository flightsRepository;

    @InjectMocks
    private FlightsService flightsService;

    private static final Airport AMS = AirportFactory.newAirport("AMS", "Schiphol", "Amsterdam", -4.5d);
    private static final Airport LHR = AirportFactory.newAirport("LHR", "Heathrow", "London", 120d);
    private static final Airport FRA = AirportFactory.newAirport("FRA", "Frankfurt am Main", "Frankfurt", 350d);

    private static final Flight LK001 = FlightFactory.newFlight("LK001", "09:55", AMS, LHR, 123.0d);
    private static final Flight LK002 = FlightFactory.newFlight("LK002", "13:15", LHR, AMS, 321.0d);
    private static final Flight LK003 = FlightFactory.newFlight("LK003", "10:45", AMS, FRA, 456.0d);
    private static final Flight LK004 = FlightFactory.newFlight("LK004", "14:35", FRA, LHR, 654.0d);

    @BeforeEach
    public void setUp() {
        when(flightsRepository.findByFromCodeAndToCodeOrOrderByTimeAsc(eq("AMS"), eq("FRA"), any()))
                .thenReturn(Arrays.asList(LK001));

        when(flightsRepository.findByToDescriptionLike(argThat(new CaseInsensitiveEquals("AM"))))
                .thenReturn(Arrays.asList(LK002, LK003));

        when(flightsRepository.findAll()).thenReturn(Arrays.asList(LK002, LK004, LK001, LK003));

        when(flightsRepository.findByFromDescriptionLike(argThat(new CaseInsensitiveEquals("AM"))))
                .thenReturn(Arrays.asList(LK001, LK003, LK004));

        when(flightsRepository.findByFromToDescriptionLike(eq("AMS"), argThat(new CaseInsensitiveEquals("AM"))))
                .thenReturn(Arrays.asList(LK003));
    }

    @Test
    public void getFlightsFromToFound() {
        List<Flight> results = flightsService.getFlightsFromTo("AMS", "FRA");

        Assert.notNull(results, "A list (empty or not) is expected");
        assertThat(results, contains(LK001));
    }

    @Test
    public void getFlightsFromToNotFound() {
        List<Flight> results = flightsService.getFlightsFromTo("AMS", "MAD");

        Assert.notNull(results, "A list (empty or not) is expected");
        assertThat(results, empty());
    }

    @Test
    public void getFlightsWhereFromDescriptionMatches() {
        List<Flight> results = flightsService.getFlightsWhereFromDescriptionMatches("am");

        Assert.notNull(results, "A list (empty or not) is expected");
        assertThat(results, contains(LK001, LK003, LK004));
    }

    @Test
    public void getFlightsWhereToDescriptionMatches() {
        List<Flight> results = flightsService.getFlightsWhereToDescriptionMatches("am");

        Assert.notNull(results, "A list (empty or not) is expected");
        assertThat(results, contains(LK002, LK003));
    }

    @Test
    public void getAllAirports() {
        List<Flight.AirportData> results = flightsService.getAllAirports();

        Assert.notNull(results, "A list (empty or not) is expected");
        assertThat(results, containsInAnyOrder(buildAirportData(AMS), buildAirportData(LHR), buildAirportData(FRA)));
    }

    @Test
    public void getAirportsFromWhereFromDescriptionMatches() {
        List<Flight.AirportData> results = flightsService.getAirportsFromWhereFromDescriptionMatches("am");

        Assert.notNull(results, "A list (empty or not) is expected");
        assertThat(results, containsInAnyOrder(buildAirportData(AMS), buildAirportData(FRA)));
    }

    @Test
    public void getAirportsToWhereToDescriptionMatches() {
        List<Flight.AirportData> results = flightsService.getAirportsToWhereToDescriptionMatches("am");

        Assert.notNull(results, "A list (empty or not) is expected");
        assertThat(results, containsInAnyOrder(buildAirportData(AMS), buildAirportData(FRA)));
    }

    @Test
    public void getAirportsToWhereFromAndToDescriptionMatches() {
        List<Flight.AirportData> results = flightsService.getAirportsToWhereFromAndToDescriptionMatches("AMS", "am");

        Assert.notNull(results, "A list (empty or not) is expected");
        assertThat(results, containsInAnyOrder(buildAirportData(FRA)));
    }
}

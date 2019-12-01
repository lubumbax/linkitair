package com.lubumbax.linkitair.flights.controller;

import com.lubumbax.linkitair.flights.TestAppConfig;
import com.lubumbax.linkitair.flights.model.Airport;
import com.lubumbax.linkitair.flights.model.Flight;
import com.lubumbax.linkitair.flights.service.FlightsService;
import com.lubumbax.linkitair.flights.test.factory.AirportFactory;
import com.lubumbax.linkitair.flights.test.factory.FlightFactory;

import com.lubumbax.linkitair.flights.test.matcher.CaseInsensitiveLike;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = {FlightsController.class})
@Import(TestAppConfig.class)
public class FlightsControllerTest {
    @Autowired
    private FlightsService flightsService;

    @Autowired
    private FlightsController flightsController;

    private MockMvc mvc;

    private static final Airport AMS = AirportFactory.newAirport("AMS", "Schiphol", "Amsterdam", -4.5d);
    private static final Airport LHR = AirportFactory.newAirport("LHR", "Heathrow", "London", 120d);
    private static final Airport FRA = AirportFactory.newAirport("FRA", "Frankfurt am Main", "Frankfurt", 350d);

    private static final Flight LK001 = FlightFactory.newFlight("LK001", "09:55", AMS, LHR, 123.0d);
    private static final Flight LK002 = FlightFactory.newFlight("LK002", "13:15", LHR, AMS, 321.0d);
    private static final Flight LK003 = FlightFactory.newFlight("LK003", "10:45", AMS, FRA, 456.0d);
    private static final Flight LK004 = FlightFactory.newFlight("LK004", "14:35", FRA, LHR, 654.0d);

    private static final Flight.AirportData AMS_DATA = LK001.getFrom();
    private static final Flight.AirportData LHR_DATA = LK002.getFrom();
    private static final Flight.AirportData FRA_DATA = LK004.getFrom();

    private static final String AMS_DESCRIPTION = "AMS - Schiphol (Amsterdam)";
    private static final String LHR_DESCRIPTION = "LHR - Heathrow (London)";
    private static final String FRA_DESCRIPTION = "FRA - Frankfurt am Main (Frankfurt)";

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(flightsController).build();
    }

    @Test
    public void findFrom_happyPath() throws Exception {
        when(flightsService.getFlightsFromTo(any(), any())).thenReturn(Collections.emptyList());

        when(flightsService.getFlightsFromTo("AMS", "LHR")).thenReturn(Arrays.asList(LK001));
        when(flightsService.getFlightsFromTo("LHR", "AMS")).thenReturn(Arrays.asList(LK002));
        when(flightsService.getFlightsFromTo("AMS", "FRA")).thenReturn(Arrays.asList(LK003));
        when(flightsService.getFlightsFromTo("FRA", "LHR")).thenReturn(Arrays.asList(LK004));

        mvc.perform(MockMvcRequestBuilders
                .get("/flights/from/{from}/to/{to}","AMS", "LHR")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].number", is("LK001")))
                .andExpect(jsonPath("$[0].from.code", is("AMS")))
                .andExpect(jsonPath("$[0].to.code", is("LHR")));
    }

    @Test
    public void findFrom_noneFound() throws Exception {
        when(flightsService.getFlightsFromTo(any(), any())).thenReturn(Collections.emptyList());

        when(flightsService.getFlightsFromTo("AMS", "LHR")).thenReturn(Arrays.asList(LK001));
        when(flightsService.getFlightsFromTo("LHR", "AMS")).thenReturn(Arrays.asList(LK002));
        when(flightsService.getFlightsFromTo("AMS", "FRA")).thenReturn(Arrays.asList(LK003));
        when(flightsService.getFlightsFromTo("FRA", "LHR")).thenReturn(Arrays.asList(LK004));

        mvc.perform(MockMvcRequestBuilders
                .get("/flights/from/{from}/to/{to}","AMS", "MAD")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void findAirportsFrom_matchTwo() throws Exception {
        when(flightsService.getAirportsFromWhereFromDescriptionMatches(argThat(new CaseInsensitiveLike(AMS_DESCRIPTION, FRA_DESCRIPTION)))).thenReturn(Arrays.asList(AMS_DATA, FRA_DATA));

        mvc.perform(MockMvcRequestBuilders
                .get("/flights/airports/from/{fromMatch}","am")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].code", is("AMS")))
                .andExpect(jsonPath("$[1].code", is("FRA")));
    }

    public void findAirportsFrom_matchOne() throws Exception {
        when(flightsService.getAirportsFromWhereFromDescriptionMatches(argThat(new CaseInsensitiveLike(AMS_DESCRIPTION)))).thenReturn(Arrays.asList(AMS_DATA));

        mvc.perform(MockMvcRequestBuilders
                .get("/flights/airports/from/{fromMatch}","ams")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}

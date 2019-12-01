package com.lubumbax.linkitair.flights.test.factory;

import com.lubumbax.linkitair.flights.model.Airport;
import com.lubumbax.linkitair.flights.model.Flight;

import java.math.BigDecimal;

public class FlightFactory {
    public static Flight newFlight(String number, String time, Airport from, Airport to, double price) {
        return Flight.builder()
                .number(number)
                .time(time)
                .from(buildAirportData(from))
                .to(buildAirportData(to))
                .price(BigDecimal.valueOf(price))
                .build();
    }

    public static Flight.AirportData buildAirportData(Airport airport) {
        Flight.AirportData fa = new Flight.AirportData();
        fa.setCode(airport.getCode());
        fa.setDescription(airport.getCode() + " - " + airport.getName() + " (" + airport.getCity() + ")");
        return fa;
    }
}

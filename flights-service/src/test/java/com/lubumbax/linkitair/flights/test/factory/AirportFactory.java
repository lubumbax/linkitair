package com.lubumbax.linkitair.flights.test.factory;

import com.lubumbax.linkitair.flights.model.Airport;

import java.math.BigDecimal;

public class AirportFactory {
    public static Airport newAirport(String code, String name, String city, double altitude) {
        return Airport.builder().code(code).name(name).city(city).altitude(BigDecimal.valueOf(altitude)).build();
    }
}

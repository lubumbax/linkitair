package com.lubumbax.linkitair.flights.init;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lubumbax.linkitair.flights.model.Airport;
import com.lubumbax.linkitair.flights.model.Flight;
import com.lubumbax.linkitair.flights.repository.AirportsRepository;
import com.lubumbax.linkitair.flights.repository.FlightsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class ImportData implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private AirportsRepository airportsRepository;

    @Autowired
    private FlightsRepository flightsRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Importing airports");
        importAirports(true);

        log.info("Importing flights");
        importFlights(true);
    }

    private void importFlights(boolean updating) {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = TypeReference.class.getResourceAsStream("/db/flights.json");
        TypeReference<List<Flight>> typeReference = new TypeReference<List<Flight>>(){};
        AtomicInteger count = new AtomicInteger();
        try {
            List<Flight> jsonList = mapper.readValue(is, typeReference);
            jsonList.forEach(f -> {
                if (updating || flightsRepository.findById(f.getNumber()).isEmpty()) {
                    flightsRepository.save(f);
                    count.getAndIncrement();
                }
            });
            log.info("{} flights saved!", count.get());
        } catch (IOException e){
            log.error("Unable to map Flights from file to list: {}", e.getMessage());
        }
    }

    private void importAirports(boolean updating) {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = TypeReference.class.getResourceAsStream("/db/airports.json");
        TypeReference<List<Airport>> typeReference = new TypeReference<List<Airport>>(){};
        AtomicInteger count = new AtomicInteger();
        try {
            List<Airport> jsonList = mapper.readValue(is, typeReference);
            jsonList.forEach(a -> {
                if (updating || airportsRepository.findById(a.getCode()).isEmpty()) {
                    airportsRepository.save(a);
                    count.getAndIncrement();
                }
            });
            log.info("{} airports saved!", count.get());
        } catch (IOException e){
            log.error("Unable to map Airports from file to list: {}", e.getMessage());
        }
    }
}

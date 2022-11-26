package com.lubumbax.linkitair.flights.init;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lubumbax.linkitair.flights.config.LinkitAirProperties;
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
    private LinkitAirProperties properties;

    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        boolean updating = properties.getInitialData().isUpdating();
        boolean deleteFirst = properties.getInitialData().isDeleteFirst();

        log.info("Importing airports");
        importAirports(updating, deleteFirst);

        log.info("Importing flights");
        importFlights(updating, deleteFirst);
    }

    private void importFlights(boolean updating, boolean deleteFirst) {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = TypeReference.class.getResourceAsStream("/db/flights.json");
        TypeReference<List<Flight>> typeReference = new TypeReference<List<Flight>>(){};
        AtomicInteger count = new AtomicInteger();
        try {
            List<Flight> jsonList = mapper.readValue(is, typeReference);
            if (deleteFirst) {
                log.info("Deleting all existing flights");
                flightsRepository.deleteAll();
            }
            jsonList.forEach(f -> {
                if (deleteFirst || updating || flightsRepository.findById(f.getNumber()).isEmpty()) {
                    flightsRepository.save(f);
                    count.getAndIncrement();
                }
            });
            log.info("{} flights saved!", count.get());
        } catch (IOException e){
            log.error("Unable to map Flights from file to list: {}", e.getMessage());
        }
    }

    private void importAirports(boolean updating, boolean deleteFirst) {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = TypeReference.class.getResourceAsStream("/db/airports.json");
        TypeReference<List<Airport>> typeReference = new TypeReference<List<Airport>>(){};
        AtomicInteger count = new AtomicInteger();
        try {
            List<Airport> jsonList = mapper.readValue(is, typeReference);
            if (deleteFirst) {
                log.info("Deleting all existing airports");
                flightsRepository.deleteAll();
            }
            jsonList.forEach(a -> {
                if (deleteFirst || updating || airportsRepository.findById(a.getCode()).isEmpty()) {
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

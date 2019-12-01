package com.lubumbax.linkitair.flights;

import com.lubumbax.linkitair.flights.model.Airport;
import com.lubumbax.linkitair.flights.model.Flight;
import com.lubumbax.linkitair.flights.repository.AirportsRepository;
import com.lubumbax.linkitair.flights.repository.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootApplication
@EnableSwagger2
@EnableMongoRepositories
public class FlightsServiceApplication  implements CommandLineRunner {
    @Autowired
    private AirportsRepository airportsRepository;

    @Autowired
    private FlightsRepository flightsRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(FlightsServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Airport AMS = Airport.builder().code("AMS").name("Schiphol").city("Amsterdam").build();
        Airport LHR = Airport.builder().code("LHR").name("Heathrow").city("London").build();
        Airport FRA = Airport.builder().code("FRA").name("Frankfurt am Main").city("Frankfurt").build();

        if (! mongoTemplate.collectionExists("airports")) {
            airportsRepository.deleteAll();
            airportsRepository.saveAll(Arrays.asList(AMS, LHR, FRA));
        }

        if (! mongoTemplate.collectionExists("flights")) {
            flightsRepository.deleteAll();
            flightsRepository.saveAll(Arrays.asList(
                    Flight.builder().number("LK001")
                            .time("09:55")
                            .from(buildAirportData(AMS))
                            .to(buildAirportData(LHR))
                            .price(BigDecimal.valueOf(123.0d))
                            .build(),
                    Flight.builder().number("LK002")
                            .time("13:15")
                            .from(buildAirportData(LHR))
                            .to(buildAirportData(AMS))
                            .price(BigDecimal.valueOf(321.0d))
                            .build(),
                    Flight.builder().number("LK003")
                            .time("10:45")
                            .from(buildAirportData(AMS))
                            .to(buildAirportData(FRA))
                            .price(BigDecimal.valueOf(456.0d))
                            .build(),
                    Flight.builder().number("LK004")
                            .time("14:35")
                            .from(buildAirportData(FRA))
                            .to(buildAirportData(LHR))
                            .price(BigDecimal.valueOf(654))
                            .build()
            ));
        }
    }

    private Flight.AirportData buildAirportData(Airport airport) {
        Flight.AirportData fa = new Flight.AirportData();
        fa.setCode(airport.getCode());
        fa.setDescription(airport.getCode() + " - " + airport.getName() + " (" + airport.getCity() + ")");
        return fa;
    }
}

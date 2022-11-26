package com.lubumbax.linkitair.flights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class FlightsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlightsServiceApplication.class, args);
    }
}

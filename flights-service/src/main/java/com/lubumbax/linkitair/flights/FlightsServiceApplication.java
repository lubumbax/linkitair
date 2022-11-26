package com.lubumbax.linkitair.flights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@ConfigurationPropertiesScan
public class FlightsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlightsServiceApplication.class, args);
    }
}

package com.lubumbax.linkitair.flights;

import com.lubumbax.linkitair.flights.service.FlightsService;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@TestConfiguration
@DataMongoTest
@EnableMongoRepositories(basePackages = {"com.lubumbax.linkitair.flights.repository"})
public class TestAppConfig {
    @Bean
    public FlightsService languageService() {
        return Mockito.mock(FlightsService.class);
    }
}

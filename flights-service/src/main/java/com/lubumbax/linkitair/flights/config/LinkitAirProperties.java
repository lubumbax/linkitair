package com.lubumbax.linkitair.flights.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("linkitair")
@Data
public class LinkitAirProperties {
    InitialDataProperties initialData = new InitialDataProperties();

    @Data
    public static class InitialDataProperties {
        boolean updating = true;
        boolean deleteFirst = false;
    }
}

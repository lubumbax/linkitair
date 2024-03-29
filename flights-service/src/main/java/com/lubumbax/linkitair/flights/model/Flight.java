package com.lubumbax.linkitair.flights.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "flights")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class Flight {
    @Id
    @Schema(description = "Flight number")
    private String number;

    @Schema(description = "Airline that handles the flight")
    private String Airline;

    @Schema(description = "From airport")
    private Flight.AirportData from;

    @Schema(description = "To airport")
    private Flight.AirportData to;

    @Schema(description = "Departure time, local time of the departure city")
    private String time;

    @Schema(description = "Price")
    private BigDecimal price;

    @Data
    @Schema(description = "Airport data of a given Flight")
    public static class AirportData {
        @Schema(description = "Airport code", example = "AMS")
        private String code;

        @Schema(
                description = "Combination of the airport code, name and city",
                example = "AMS - Schiphol (Amsterdam)"
        )
        private String description;
    }
}

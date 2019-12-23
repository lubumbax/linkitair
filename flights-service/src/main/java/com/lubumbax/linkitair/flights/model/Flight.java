package com.lubumbax.linkitair.flights.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@ApiModel
@Document(collection = "flights")
public class Flight {
    @ApiModelProperty(value = "Flight number")
    @Id
    private String number;

    @ApiModelProperty(value = "From airport")
    private Flight.AirportData from;

    @ApiModelProperty(value = "To airport")
    private Flight.AirportData to;

    @ApiModelProperty(value = "Departure time, local time of the departure city")
    private String time;

    @ApiModelProperty(value = "Price")
    private BigDecimal price;

    @Data
    @ApiModel(description = "Airport data of a given Flight")
    public static class AirportData {
        @ApiModelProperty(value = "Airport code", example = "AMS")
        @Id
        private String code;

        @ApiModelProperty(
                value = "Airport description label",
                notes = "Combination of the airport code, name and city",
                example = "AMS - Schiphol (Amsterdam)"
        )
        private String description;
    }
}

package com.lubumbax.linkitair.flights.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "airports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class Airport {
    @Schema(description = "Code of the airport")
    @Id
    private String code;

    @Schema(description = "Name of the airport")
    private String name;

    @Schema(description = "City of the airport")
    private String city;

    @Schema(description = "Altitude of the airport in meters")
    private BigDecimal altitude;
}

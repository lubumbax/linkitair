package com.lubumbax.linkitair.flights.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@Builder
@ApiModel
public class Airport {
    @ApiModelProperty(value = "Code of the airport")
    @Id
    private String code;

    @ApiModelProperty(value = "Name of the airport")
    private String name;

    @ApiModelProperty(value = "City of the airport")
    private String city;

    @ApiModelProperty(value = "Altitude of the airport in meters")
    private BigDecimal altitude;
}

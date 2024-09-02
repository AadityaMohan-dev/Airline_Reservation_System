package com.aaditya.airline_reservation_system.DTO;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
@Getter
@Setter
public class ResFlightDTO {
    @NonNull
    private Long id;
    @NonNull
    private String airlineName;
    @NonNull
    private String flightNumber;
    @NonNull
    private String DepartureTime;

    private String DestinationAirport;
    @NonNull
    private String airportName;
    @NonNull
    private LocalDate date;


}

package com.aaditya.airline_reservation_system.DTO;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@Getter
@Setter
public class ReqFlightDTO {
    @NonNull
    private String airlineName;
    @NonNull
    private String flightNumber;
    @NonNull
    private LocalTime DepartureTime;
    @NonNull
    private String DestinationAirport;
    @NonNull
    private String airportName;
}

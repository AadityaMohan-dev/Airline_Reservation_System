package com.aaditya.airline_reservation_system.DTO;

import com.aaditya.airline_reservation_system.Entity.Ticket;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Component
@Getter
@Setter
public class ResFlightDTO {
    @NonNull
    private String airlineName;
    @NonNull
    private String flightNumber;
    @NonNull
    private String DepartureTime;
    @NonNull
    private String DestinationAirport;
    @NonNull
    private String airportName;
    @NonNull
    private LocalDate date;
    private LocalTime departure_time;

}

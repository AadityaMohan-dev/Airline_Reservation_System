package com.aaditya.airline_reservation_system.DTO;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Getter
@Setter
public class ResTicketDTO {
    private String ticket_number;
    private String username;
    @NonNull
    private String airline_name;
    @NonNull
    private String flightNumber;
    @NonNull
    private String destination_airport;
    @NonNull
    private String airport_name;
    @NonNull
    private LocalDate date;
    private String departure_time;
}

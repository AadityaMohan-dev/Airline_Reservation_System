package com.aaditya.airline_reservation_system.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long flight_id;
    @Column(nullable = false)
    private String airlineName;
    @Column(nullable = false)
    private String flightNumber;
//    @Column(nullable = false)
    private LocalTime DepartureTime;
    private String DestinationAirport;
    @Column(nullable = false)
    private String airportName;
    private LocalDate date = LocalDate.now();

    @OneToMany(mappedBy = "flight")
    private Set<Ticket> tickets;
}

package com.aaditya.airline_reservation_system.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passenger_id;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String email;

    @OneToOne
    private User user;
}

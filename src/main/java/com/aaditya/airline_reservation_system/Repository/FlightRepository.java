package com.aaditya.airline_reservation_system.Repository;

import com.aaditya.airline_reservation_system.Entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight,Long> {
}

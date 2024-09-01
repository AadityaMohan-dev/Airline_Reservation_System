package com.aaditya.airline_reservation_system.Repository;

import com.aaditya.airline_reservation_system.Entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger,Long> {
}

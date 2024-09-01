package com.aaditya.airline_reservation_system.Repository;

import com.aaditya.airline_reservation_system.Entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}

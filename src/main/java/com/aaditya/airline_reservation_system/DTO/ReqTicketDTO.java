package com.aaditya.airline_reservation_system.DTO;
import com.aaditya.airline_reservation_system.Entity.Flight;
import com.aaditya.airline_reservation_system.Entity.Passenger;
import com.aaditya.airline_reservation_system.Entity.Ticket;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Component;



@Component
@Getter
@Setter
public class ReqTicketDTO {
    @NonNull
    private String username;
    private Passenger passenger;
    private Flight flight;
}

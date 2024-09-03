package com.aaditya.airline_reservation_system.servicesTest;

import com.aaditya.airline_reservation_system.DTO.ReqTicketDTO;
import com.aaditya.airline_reservation_system.DTO.ResTicketDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Entity.Flight;
import com.aaditya.airline_reservation_system.Entity.Passenger;
import com.aaditya.airline_reservation_system.Entity.Ticket;
import com.aaditya.airline_reservation_system.Entity.User;
import com.aaditya.airline_reservation_system.Repository.FlightRepository;
import com.aaditya.airline_reservation_system.Repository.PassengerRepository;
import com.aaditya.airline_reservation_system.Repository.TicketRepository;
import com.aaditya.airline_reservation_system.Services.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private FlightRepository flightRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTicket_Success() {
        // Setup
        Long passengerId = 1L;
        Long flightId = 1L;
        ReqTicketDTO reqTicketDTO = new ReqTicketDTO();
        reqTicketDTO.setUsername("testUser");
        reqTicketDTO.setPassenger_id(passengerId);
        reqTicketDTO.setFlight_id(flightId);

        Passenger passenger = new Passenger();
        passenger.setPassenger_id(passengerId);
        passenger.setUser(new User("testUser"));

        Flight flight = new Flight();
        flight.setAirlineName("TestAirline");
        flight.setAirportName("TestAirport");
        flight.setDepartureTime("10:00");
        flight.setDestinationAirport("DestinationAirport");
        flight.setFlightNumber("1234");

        Ticket ticket = new Ticket();
        ticket.setTicket_number("1-0001");
        ticket.setPassenger(passenger);
        ticket.setFlight(flight);

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Execute
        ResTicketDTO result = ticketService.createTicket(reqTicketDTO);

        // Verify
        assertNotNull(result);
        assertEquals("1-0001", result.getTicket_number());
        assertEquals("testUser", result.getUsername());
        assertEquals("TestAirline", result.getAirline_name());
        assertEquals(LocalDate.now(), result.getDate());
        assertEquals("TestAirport", result.getAirport_name());
        assertEquals("10:00", result.getDeparture_time());
        assertEquals("DestinationAirport", result.getDestination_airport());
        assertEquals("1234", result.getFlightNumber());
    }

    @Test
    void deleteTicketById_Success() {
        Long ticketId = 1L;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(new Ticket()));

        ResponseDTO response = ticketService.deleteTicketById(ticketId);

        assertNotNull(response);
        assertEquals("ticket deleted with id : 1", response.getMessage());
        verify(ticketRepository, times(1)).deleteById(ticketId);
    }

    @Test
    void deleteTicketById_NotFound() {
        Long ticketId = 1L;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        ResponseDTO response = ticketService.deleteTicketById(ticketId);

        assertNull(response);
        verify(ticketRepository, never()).deleteById(ticketId);
    }

    @Test
    void getAllTickets_Success() {
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket();
        ticket.setTicket_number("1-0001");
        ticket.setPassenger(new Passenger());
        ticket.setFlight(new Flight());
        tickets.add(ticket);

        when(ticketRepository.findAll()).thenReturn(tickets);

        List<ResTicketDTO> result = ticketService.getAllTickets();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getTicketById_Success() {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setTicket_number("1-0001");
        ticket.setPassenger(new Passenger());
        ticket.setFlight(new Flight());

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        ResTicketDTO result = ticketService.getTicketById(ticketId);

        assertNotNull(result);
        assertEquals("1-0001", result.getTicket_number());
    }

    @Test
    void getTicketById_NotFound() {
        Long ticketId = 1L;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        ResTicketDTO result = ticketService.getTicketById(ticketId);

        assertNull(result);
    }
}

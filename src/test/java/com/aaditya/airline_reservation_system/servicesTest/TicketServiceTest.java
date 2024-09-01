package com.aaditya.airline_reservation_system.servicesTest;

import com.aaditya.airline_reservation_system.DTO.ReqTicketDTO;
import com.aaditya.airline_reservation_system.DTO.ResTicketDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Entity.Flight;
import com.aaditya.airline_reservation_system.Entity.Passenger;
import com.aaditya.airline_reservation_system.Entity.Ticket;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private FlightRepository flightRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTicket_Success() {
        Passenger passenger = new Passenger();
        passenger.setPassenger_id(1L);
        Flight flight = new Flight();
        flight.setAirlineName("Airline");
        flight.setAirportName("Airport");
        flight.setDepartureTime(LocalTime.from(LocalDate.now()));
        flight.setDestinationAirport("Destination");
        flight.setFlightNumber("FL123");

        Ticket ticket = new Ticket();
        ticket.setTicket_number("1-0001");

        ReqTicketDTO reqTicketDTO = new ReqTicketDTO();
        reqTicketDTO.setPassenger(passenger);

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        ResTicketDTO result = ticketService.createTicket(reqTicketDTO, 1L, 1L);

        assertNotNull(result);
        assertEquals("1-0001", result.getTicket_number());
        assertEquals("Airline", result.getAirline_name());
        assertEquals("Airport", result.getAirport_name());
        assertEquals(LocalDate.now(), result.getDate());
        assertEquals("Destination", result.getDestination_airport());
        assertEquals("FL123", result.getFlightNumber());
    }

    @Test
    public void createTicket_PassengerNotFound() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());
        ReqTicketDTO reqTicketDTO = new ReqTicketDTO();

        ResTicketDTO result = ticketService.createTicket(reqTicketDTO, 1L, 1L);

        assertNull(result);
    }

    @Test
    public void createTicket_FlightNotFound() {
        Passenger passenger = new Passenger();
        passenger.setPassenger_id(1L);
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        ReqTicketDTO reqTicketDTO = new ReqTicketDTO();

        ResTicketDTO result = ticketService.createTicket(reqTicketDTO, 1L, 1L);

        assertNull(result);
    }

    @Test
    public void deleteTicketById_Success() {
        Ticket ticket = new Ticket();
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        ResponseDTO response = ticketService.deleteTicketById(1L);

        assertNotNull(response);
        assertEquals("ticket deleted with id : 1", response.getMessage());
        verify(ticketRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteTicketById_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseDTO response = ticketService.deleteTicketById(1L);

        assertNull(response);
    }

    @Test
    public void getAllTickets_Success() {
        Ticket ticket = new Ticket();
        ticket.setTicket_number("1-0001");
        Flight flight = new Flight();
        flight.setAirlineName("Airline");
        flight.setAirportName("Airport");
        flight.setDepartureTime(LocalTime.from(LocalDate.now()));
        flight.setDestinationAirport("Destination");
        flight.setFlightNumber("FL123");
        ticket.setFlight(flight);

        Passenger passenger = new Passenger();
        passenger.setPassenger_id(1L);
        ticket.setPassenger(passenger);

        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);

        when(ticketRepository.findAll()).thenReturn(tickets);

        List<ResTicketDTO> result = ticketService.getAllTickets();

        assertNotNull(result);
        assertEquals(1, result.size());
        ResTicketDTO ticketDTO = result.get(0);
        assertEquals("1-0001", ticketDTO.getTicket_number());
        assertEquals("Airline", ticketDTO.getAirline_name());
        assertEquals("Airport", ticketDTO.getAirport_name());
        assertEquals(LocalDate.now(), ticketDTO.getDate());
        assertEquals("Destination", ticketDTO.getDestination_airport());
        assertEquals("FL123", ticketDTO.getFlightNumber());
    }

    @Test
    public void getAllTickets_NoTickets() {
        when(ticketRepository.findAll()).thenReturn(new ArrayList<>());

        List<ResTicketDTO> result = ticketService.getAllTickets();

        assertNull(result);
    }

    @Test
    public void getTicketById_Success() {
        Ticket ticket = new Ticket();
        ticket.setTicket_number("1-0001");
        Flight flight = new Flight();
        flight.setAirlineName("Airline");
        flight.setAirportName("Airport");
        flight.setDepartureTime(LocalTime.from(LocalDate.now()));
        flight.setDestinationAirport("Destination");
        flight.setFlightNumber("FL123");
        ticket.setFlight(flight);

        Passenger passenger = new Passenger();
        passenger.setPassenger_id(1L);
        ticket.setPassenger(passenger);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        ResTicketDTO result = ticketService.getTicketById(1L);

        assertNotNull(result);
        assertEquals("1-0001", result.getTicket_number());
        assertEquals("Airline", result.getAirline_name());
        assertEquals("Airport", result.getAirport_name());
        assertEquals(LocalDate.now(), result.getDate());
        assertEquals("Destination", result.getDestination_airport());
        assertEquals("FL123", result.getFlightNumber());
    }

    @Test
    public void getTicketById_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        ResTicketDTO result = ticketService.getTicketById(1L);

        assertNull(result);
    }
}

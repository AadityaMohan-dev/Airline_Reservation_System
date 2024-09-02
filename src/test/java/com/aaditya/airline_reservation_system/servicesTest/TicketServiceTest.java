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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTicket_Success() {
        ReqTicketDTO reqTicketDTO = new ReqTicketDTO();
        Long flightId = 1L;
        Long passengerId = 2L;

        Passenger passenger = new Passenger();
        passenger.setPassenger_id(passengerId);
        passenger.setUser(new User());
        passenger.getUser().setUsername("john_doe");

        Flight flight = new Flight();
        flight.setAirlineName("Airline");
        flight.setAirportName("Airport");
        flight.setDepartureTime(LocalTime.of(10, 30));
        flight.setDestinationAirport("Destination");
        flight.setFlightNumber("FL123");

        Ticket ticket = new Ticket();
        ticket.setTicket_number("2-0001");
        ticket.setFlight(flight);
        ticket.setPassenger(passenger);

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        ResTicketDTO ticketDTO = ticketService.createTicket(reqTicketDTO, flightId, passengerId);

        assertNotNull(ticketDTO);
        assertEquals(ticket.getTicket_number(), ticketDTO.getTicket_number());
        assertEquals(ticket.getPassenger().getUser().getUsername(), ticketDTO.getUsername());
        assertEquals(ticket.getFlight().getAirlineName(), ticketDTO.getAirline_name());
        assertEquals(LocalDate.now(), ticketDTO.getDate());
        assertEquals(ticket.getFlight().getAirportName(), ticketDTO.getAirport_name());
        assertEquals(ticket.getFlight().getDepartureTime(), ticketDTO.getDeparture_time());
        assertEquals(ticket.getFlight().getDestinationAirport(), ticketDTO.getDestination_airport());
        assertEquals(ticket.getFlight().getFlightNumber(), ticketDTO.getFlightNumber());
    }

    @Test
    void testCreateTicket_FlightNotFound() {
        ReqTicketDTO reqTicketDTO = new ReqTicketDTO();
        Long flightId = 1L;
        Long passengerId = 2L;

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(new Passenger()));
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        ResTicketDTO ticketDTO = ticketService.createTicket(reqTicketDTO, flightId, passengerId);

        assertNull(ticketDTO);
    }

    @Test
    void testDeleteTicketById_Success() {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setTicket_id(ticketId);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        doNothing().when(ticketRepository).deleteById(ticketId);

        ResponseDTO responseDTO = ticketService.deleteTicketById(ticketId);
        assertNotNull(responseDTO);
        assertEquals("ticket deleted with id : " + ticketId, responseDTO.getMessage());
    }

    @Test
    void testDeleteTicketById_NotFound() {
        Long ticketId = 1L;

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        ResponseDTO responseDTO = ticketService.deleteTicketById(ticketId);
        assertNull(responseDTO);
    }

    @Test
    void testGetAllTickets_Success() {
        Ticket ticket = new Ticket();
        ticket.setTicket_number("2-0001");
        ticket.setFlight(new Flight());
        ticket.getFlight().setAirlineName("Airline");
        ticket.getFlight().setAirportName("Airport");
        ticket.getFlight().setDepartureTime(LocalTime.of(10, 30));
        ticket.getFlight().setDestinationAirport("Destination");
        ticket.getFlight().setFlightNumber("FL123");
        ticket.setPassenger(new Passenger());
        ticket.getPassenger().setUser(new User());
        ticket.getPassenger().getUser().setUsername("john_doe");

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket);

        when(ticketRepository.findAll()).thenReturn(ticketList);

        List<ResTicketDTO> ticketDTOList = ticketService.getAllTickets();

        assertNotNull(ticketDTOList);
        assertEquals(1, ticketDTOList.size());
        ResTicketDTO ticketDTO = ticketDTOList.get(0);
        assertEquals(ticket.getTicket_number(), ticketDTO.getTicket_number());
        assertEquals(ticket.getPassenger().getUser().getUsername(), ticketDTO.getUsername());
        assertEquals(ticket.getFlight().getAirlineName(), ticketDTO.getAirline_name());
        assertEquals(LocalDate.now(), ticketDTO.getDate());
        assertEquals(ticket.getFlight().getAirportName(), ticketDTO.getAirport_name());
        assertEquals(ticket.getFlight().getDepartureTime(), ticketDTO.getDeparture_time());
        assertEquals(ticket.getFlight().getDestinationAirport(), ticketDTO.getDestination_airport());
        assertEquals(ticket.getFlight().getFlightNumber(), ticketDTO.getFlightNumber());
    }

    @Test
    void testGetAllTickets_NoTickets() {
        when(ticketRepository.findAll()).thenReturn(new ArrayList<>());

        List<ResTicketDTO> ticketDTOList = ticketService.getAllTickets();

        assertNull(ticketDTOList);
    }

    @Test
    void testGetTicketById_Found() {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setTicket_number("2-0001");
        ticket.setFlight(new Flight());
        ticket.getFlight().setAirlineName("Airline");
        ticket.getFlight().setAirportName("Airport");
        ticket.getFlight().setDepartureTime(LocalTime.of(10, 30));
        ticket.getFlight().setDestinationAirport("Destination");
        ticket.getFlight().setFlightNumber("FL123");
        ticket.setPassenger(new Passenger());
        ticket.getPassenger().setUser(new User());
        ticket.getPassenger().getUser().setUsername("john_doe");

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        ResTicketDTO ticketDTO = ticketService.getTicketById(ticketId);

        assertNotNull(ticketDTO);
        assertEquals(ticket.getTicket_number(), ticketDTO.getTicket_number());
        assertEquals(ticket.getPassenger().getUser().getUsername(), ticketDTO.getUsername());
        assertEquals(ticket.getFlight().getAirlineName(), ticketDTO.getAirline_name());
        assertEquals(LocalDate.now(), ticketDTO.getDate());
        assertEquals(ticket.getFlight().getAirportName(), ticketDTO.getAirport_name());
        assertEquals(ticket.getFlight().getDepartureTime(), ticketDTO.getDeparture_time());
        assertEquals(ticket.getFlight().getDestinationAirport(), ticketDTO.getDestination_airport());
        assertEquals(ticket.getFlight().getFlightNumber(), ticketDTO.getFlightNumber());
    }

    @Test
    void testGetTicketById_NotFound() {
        Long ticketId = 1L;

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        ResTicketDTO ticketDTO = ticketService.getTicketById(ticketId);

        assertNull(ticketDTO);
    }
}

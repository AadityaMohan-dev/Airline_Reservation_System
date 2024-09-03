//package com.aaditya.airline_reservation_system.servicesTest;
//
//
//import com.aaditya.airline_reservation_system.DTO.ReqTicketDTO;
//import com.aaditya.airline_reservation_system.DTO.ResTicketDTO;
//import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
//import com.aaditya.airline_reservation_system.Entity.Flight;
//import com.aaditya.airline_reservation_system.Entity.Passenger;
//import com.aaditya.airline_reservation_system.Entity.Ticket;
//import com.aaditya.airline_reservation_system.Repository.FlightRepository;
//import com.aaditya.airline_reservation_system.Repository.PassengerRepository;
//import com.aaditya.airline_reservation_system.Repository.TicketRepository;
//import com.aaditya.airline_reservation_system.Services.TicketService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TicketServiceTest {
//
//    @InjectMocks
//    private TicketService ticketService;
//
//    @Mock
//    private TicketRepository ticketRepository;
//
//    @Mock
//    private PassengerRepository passengerRepository;
//
//    @Mock
//    private FlightRepository flightRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateTicket_Success() {
//        // Arrange
//        Passenger passenger = new Passenger();
//        passenger.setPassenger_id(1L);
//        passenger.getUser().setUsername("john_doe");
//
//        Flight flight = new Flight();
//        flight.setAirlineName("AirlineX");
//        flight.setAirportName("AirportA");
//        flight.setDepartureTime("10:30:00");
//        flight.setDestinationAirport("AirportB");
//        flight.setFlightNumber("FL123");
//
//        ReqTicketDTO reqTicketDTO = new ReqTicketDTO();
//        reqTicketDTO.setPassenger_id(1L);
//        reqTicketDTO.setFlight_id(1L);
//        reqTicketDTO.setUsername("john_doe");
//
//        Ticket ticket = new Ticket();
//        ticket.setTicket_number("1-0001");
//        ticket.setFlight(flight);
//        ticket.setPassenger(passenger);
//
//        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
//        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
//        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
//
//        // Act
//        ResTicketDTO result = ticketService.createTicket(reqTicketDTO);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("1-0001", result.getTicket_number());
//        assertEquals("john_doe", result.getUsername());
//        assertEquals("AirlineX", result.getAirline_name());
//        assertEquals(LocalDate.now(), result.getDate());
//        assertEquals("AirportA", result.getAirport_name());
//        assertEquals(LocalTime.of(10, 30), result.getDeparture_time());
//        assertEquals("AirportB", result.getDestination_airport());
//        assertEquals("FL123", result.getFlightNumber());
//    }
//
//    @Test
//    void testCreateTicket_Failure() {
//        // Arrange
//        ReqTicketDTO reqTicketDTO = new ReqTicketDTO();
//        reqTicketDTO.setPassenger_id(1L);
//        reqTicketDTO.setFlight_id(1L);
//
//        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());
//        when(flightRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Act
//        ResTicketDTO result = ticketService.createTicket(reqTicketDTO);
//
//        // Assert
//        assertNull(result);
//    }
//
//    @Test
//    void testDeleteTicketById_Success() {
//        // Arrange
//        Ticket ticket = new Ticket();
//        ticket.setTicket_number("1-0001");
//
//        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
//
//        // Act
//        ResponseDTO response = ticketService.deleteTicketById(1L);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("ticket deleted with id : 1", response.getMessage());
//        verify(ticketRepository, times(1)).deleteById(1L);
//    }
//
//    @Test
//    void testDeleteTicketById_Failure() {
//        // Arrange
//        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Act
//        ResponseDTO response = ticketService.deleteTicketById(1L);
//
//        // Assert
//        assertNull(response);
//        verify(ticketRepository, never()).deleteById(anyLong());
//    }
//
//    @Test
//    void testGetAllTickets_Success() {
//        // Arrange
//        List<Ticket> tickets = new ArrayList<>();
//        Ticket ticket = new Ticket();
//        ticket.setTicket_number("1-0001");
//        tickets.add(ticket);
//
//        when(ticketRepository.findAll()).thenReturn(tickets);
//
//        // Act
//        List<ResTicketDTO> result = ticketService.getAllTickets();
//
//        // Assert
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        assertEquals("1-0001", result.get(0).getTicket_number());
//    }
//
//    @Test
//    void testGetAllTickets_Empty() {
//        // Arrange
//        when(ticketRepository.findAll()).thenReturn(new ArrayList<>());
//
//        // Act
//        List<ResTicketDTO> result = ticketService.getAllTickets();
//
//        // Assert
//        assertNull(result);
//    }
//
//    @Test
//    void testGetTicketById_Success() {
//        // Arrange
//        Ticket ticket = new Ticket();
//        ticket.setTicket_number("1-0001");
//
//        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
//
//        // Act
//        ResTicketDTO result = ticketService.getTicketById(1L);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("1-0001", result.getTicket_number());
//    }
//
//    @Test
//    void testGetTicketById_Failure() {
//        // Arrange
//        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Act
//        ResTicketDTO result = ticketService.getTicketById(1L);
//
//        // Assert
//        assertNull(result);
//    }
//}

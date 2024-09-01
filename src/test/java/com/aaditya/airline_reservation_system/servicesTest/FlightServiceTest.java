package com.aaditya.airline_reservation_system.servicesTest;

import com.aaditya.airline_reservation_system.DTO.ReqFlightDTO;
import com.aaditya.airline_reservation_system.DTO.ResFlightDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Entity.Flight;
import com.aaditya.airline_reservation_system.Repository.FlightRepository;
import com.aaditya.airline_reservation_system.Services.FlightService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    public void testCreateFlightSuccess() {
        ReqFlightDTO reqFlightDTO = new ReqFlightDTO();
        reqFlightDTO.setAirlineName("Airline");
        reqFlightDTO.setFlightNumber("FL123");
        reqFlightDTO.setDestinationAirport("Destination");
        reqFlightDTO.setAirportName("Airport");
        reqFlightDTO.setDepartureTime(LocalTime.of(10, 30));

        Flight flight = new Flight();
        flight.setFlightNumber("FL123");
        flight.setDate(LocalDate.now());
        flight.setAirlineName("Airline");
        flight.setAirportName("Airport");
        flight.setDepartureTime(LocalTime.of(10, 30));
        flight.setDestinationAirport("Destination");

        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        ResFlightDTO flightDTO = flightService.createFlight(reqFlightDTO);

        assertNotNull(flightDTO);
        assertEquals("FL123", flightDTO.getFlightNumber());
        assertEquals(LocalDate.now(), flightDTO.getDate());
        assertEquals("Airline", flightDTO.getAirlineName());
        assertEquals("Airport", flightDTO.getAirportName());
        assertEquals(LocalTime.of(10, 30).toString(), flightDTO.getDepartureTime());
        assertEquals("Destination", flightDTO.getDestinationAirport());
    }

    @Test
    public void testGetAllFlightsSuccess() {
        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight();
        flight.setFlightNumber("FL123");
        flight.setDate(LocalDate.now());
        flight.setAirlineName("Airline");
        flight.setAirportName("Airport");
        flight.setDepartureTime(LocalTime.of(10, 30));
        flight.setDestinationAirport("Destination");
        flights.add(flight);

        when(flightRepository.findAll()).thenReturn(flights);

        List<ResFlightDTO> flightList = flightService.getAllFlights();

        assertNotNull(flightList);
        assertEquals(1, flightList.size());
        assertEquals("FL123", flightList.get(0).getFlightNumber());
    }

    @Test
    public void testGetAllFlightsEmpty() {
        when(flightRepository.findAll()).thenReturn(new ArrayList<>());

        List<ResFlightDTO> flightList = flightService.getAllFlights();

        assertNull(flightList);
    }

    @Test
    public void testGetFlightByIdSuccess() {
        Long id = 1L;
        Flight flight = new Flight();
        flight.setFlightNumber("FL123");
        flight.setDate(LocalDate.now());
        flight.setAirlineName("Airline");
        flight.setAirportName("Airport");
        flight.setDepartureTime(LocalTime.of(10, 30));
        flight.setDestinationAirport("Destination");

        when(flightRepository.findById(id)).thenReturn(Optional.of(flight));

        ResFlightDTO flightDTO = flightService.getFlightById(id);

        assertNotNull(flightDTO);
        assertEquals("FL123", flightDTO.getFlightNumber());
    }

    @Test
    public void testGetFlightByIdNotFound() {
        Long id = 1L;

        when(flightRepository.findById(id)).thenReturn(Optional.empty());

        ResFlightDTO flightDTO = flightService.getFlightById(id);

        assertNull(flightDTO);
    }

    @Test
    public void testUpdateFlightSuccess() {
        Long id = 1L;
        ReqFlightDTO reqFlightDTO = new ReqFlightDTO();
        reqFlightDTO.setFlightNumber("FL123");
        reqFlightDTO.setDestinationAirport("NewDestination");
        reqFlightDTO.setAirportName("NewAirport");
        reqFlightDTO.setDepartureTime(LocalTime.of(11, 30));

        Flight flight = new Flight();
        flight.setFlightNumber("FL123");
        flight.setDate(LocalDate.now());
        flight.setAirlineName("Airline");
        flight.setAirportName("NewAirport");
        flight.setDepartureTime(LocalTime.of(11, 30));
        flight.setDestinationAirport("NewDestination");

        when(flightRepository.findById(id)).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        ResFlightDTO flightDTO = flightService.updateFlight(id, reqFlightDTO);

        assertNotNull(flightDTO);
        assertEquals("NewDestination", flightDTO.getDestinationAirport());
        assertEquals("NewAirport", flightDTO.getAirportName());
        assertEquals(LocalTime.of(11, 30).toString(), flightDTO.getDepartureTime());
    }

    @Test
    public void testUpdateFlightNotFound() {
        Long id = 1L;
        ReqFlightDTO reqFlightDTO = new ReqFlightDTO();
        reqFlightDTO.setFlightNumber("FL123");
        reqFlightDTO.setDestinationAirport("NewDestination");

        when(flightRepository.findById(id)).thenReturn(Optional.empty());

        ResFlightDTO flightDTO = flightService.updateFlight(id, reqFlightDTO);

        assertNull(flightDTO);
    }

    @Test
    public void testDeleteFlightSuccess() {
        Long id = 1L;
        Flight flight = new Flight();

        when(flightRepository.findById(id)).thenReturn(Optional.of(flight));

        ResponseDTO responseDTO = flightService.deleteFlight(id);

        assertEquals("Flight Deleted Successfully ...", responseDTO.getMessage());
        verify(flightRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteFlightNotFound() {
        Long id = 1L;

        when(flightRepository.findById(id)).thenReturn(Optional.empty());

        ResponseDTO responseDTO = flightService.deleteFlight(id);

        assertNull(responseDTO);
    }
}

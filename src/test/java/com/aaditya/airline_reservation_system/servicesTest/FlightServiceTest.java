package com.aaditya.airline_reservation_system.servicesTest;

import com.aaditya.airline_reservation_system.DTO.ReqFlightDTO;
import com.aaditya.airline_reservation_system.DTO.ResFlightDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Entity.Flight;
import com.aaditya.airline_reservation_system.Repository.FlightRepository;
import com.aaditya.airline_reservation_system.Services.FlightService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FlightServiceTest {

    @InjectMocks
    private FlightService flightService;

    @Mock
    private FlightRepository flightRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFlight_Success() {
        ReqFlightDTO reqFlightDTO = new ReqFlightDTO();
        reqFlightDTO.setAirlineName("TestAirline");
        reqFlightDTO.setFlightNumber("1234");
        reqFlightDTO.setDestinationAirport("DestinationAirport");
        reqFlightDTO.setAirportName("AirportName");
        reqFlightDTO.setDepartureTime("10:00");

        Flight flight = new Flight();
        flight.setFlight_id(1L);
        flight.setAirlineName(reqFlightDTO.getAirlineName());
        flight.setFlightNumber(reqFlightDTO.getFlightNumber());
        flight.setDestinationAirport(reqFlightDTO.getDestinationAirport());
        flight.setAirportName(reqFlightDTO.getAirportName());
        flight.setDate(LocalDate.now());
        flight.setDepartureTime(reqFlightDTO.getDepartureTime());

        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        ResFlightDTO result = null;
        try {
            result = flightService.createFlight(reqFlightDTO);
        } catch (RuntimeException e) {
            System.err.println("Exception during createFlight: " + e.getMessage());
            e.printStackTrace();
        }

        assertNotNull(result, "Result should not be null");
        assertEquals(1L, result.getId(), "ID should match");
        assertEquals("1234", result.getFlightNumber(), "Flight number should match");
        assertEquals("TestAirline", result.getAirlineName(), "Airline name should match");
        assertEquals("AirportName", result.getAirportName(), "Airport name should match");
        assertEquals("10:00", result.getDepartureTime(), "Departure time should match");
        assertEquals("DestinationAirport", result.getDestinationAirport(), "Destination airport should match");
        assertEquals(LocalDate.now(), result.getDate(), "Date should match");
    }


    @Test
    public void testGetAllFlights_Success() {
        Flight flight1 = new Flight();
        flight1.setFlight_id(1L);
        flight1.setAirlineName("Airline1");
        flight1.setFlightNumber("FL123");
        flight1.setDestinationAirport("Destination1");
        flight1.setAirportName("Airport1");
        flight1.setDate(LocalDate.now());
        flight1.setDepartureTime("10:00:00");

        Flight flight2 = new Flight();
        flight2.setFlight_id(2L);
        flight2.setAirlineName("Airline2");
        flight2.setFlightNumber("FL456");
        flight2.setDestinationAirport("Destination2");
        flight2.setAirportName("Airport2");
        flight2.setDate(LocalDate.now());
        flight2.setDepartureTime("10:00:00");

        List<Flight> flights = Arrays.asList(flight1, flight2);

        when(flightRepository.findAll()).thenReturn(flights);

        List<ResFlightDTO> flightDTOs = flightService.getAllFlights();

        assertNotNull(flightDTOs);
        assertEquals(2, flightDTOs.size());
    }

    @Test
    public void testGetFlightById_Found() {
        Flight flight = new Flight();
        flight.setFlight_id(1L);
        flight.setAirlineName("Airline");
        flight.setFlightNumber("FL123");
        flight.setDestinationAirport("Destination");
        flight.setAirportName("Airport");
        flight.setDate(LocalDate.now());
        flight.setDepartureTime("10:00:00");

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        ResFlightDTO resFlightDTO = flightService.getFlightById(1L);

        assertNotNull(resFlightDTO);
        assertEquals("FL123", resFlightDTO.getFlightNumber());
    }

    @Test
    public void testGetFlightById_NotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        ResFlightDTO resFlightDTO = flightService.getFlightById(1L);

        assertNull(resFlightDTO);
    }

    @Test
    public void testUpdateFlight_Success() {
        ReqFlightDTO reqFlightDTO = new ReqFlightDTO();
        reqFlightDTO.setFlightNumber("FL789");
        reqFlightDTO.setDestinationAirport("NewDestination");
        reqFlightDTO.setAirportName("NewAirport");
        reqFlightDTO.setDepartureTime("10:00:00");

        Flight existingFlight = new Flight();
        existingFlight.setFlight_id(1L);
        existingFlight.setAirlineName("Airline");
        existingFlight.setFlightNumber("FL123");
        existingFlight.setDestinationAirport("OldDestination");
        existingFlight.setAirportName("OldAirport");
        existingFlight.setDate(LocalDate.now());
        existingFlight.setDepartureTime("10:00:00");

        Flight updatedFlight = new Flight();
        updatedFlight.setFlight_id(1L);
        updatedFlight.setAirlineName("Airline");
        updatedFlight.setFlightNumber(reqFlightDTO.getFlightNumber());
        updatedFlight.setDestinationAirport(reqFlightDTO.getDestinationAirport());
        updatedFlight.setAirportName(reqFlightDTO.getAirportName());
        updatedFlight.setDate(LocalDate.now());
        updatedFlight.setDepartureTime(reqFlightDTO.getDepartureTime());

        when(flightRepository.findById(1L)).thenReturn(Optional.of(existingFlight));
        when(flightRepository.save(any(Flight.class))).thenReturn(updatedFlight);

        ResFlightDTO resFlightDTO = flightService.updateFlight(1L, reqFlightDTO);

        assertNotNull(resFlightDTO);
        assertEquals("FL789", resFlightDTO.getFlightNumber());
        assertEquals("NewDestination", resFlightDTO.getDestinationAirport());
    }

    @Test
    public void testDeleteFlight_Success() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(new Flight()));

        ResponseDTO responseDTO = flightService.deleteFlight(1L);

        assertNotNull(responseDTO);
        assertEquals("Flight Deleted Successfully ...", responseDTO.getMessage());
        verify(flightRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteFlight_NotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseDTO responseDTO = flightService.deleteFlight(1L);

        assertNull(responseDTO);
        verify(flightRepository, never()).deleteById(1L);
    }
}

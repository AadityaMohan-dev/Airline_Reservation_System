package com.aaditya.airline_reservation_system.servicesTest;

import com.aaditya.airline_reservation_system.DTO.ReqFlightDTO;
import com.aaditya.airline_reservation_system.DTO.ResFlightDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Entity.Flight;
import com.aaditya.airline_reservation_system.Repository.FlightRepository;
import com.aaditya.airline_reservation_system.Services.FlightService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFlight_Success() {
        ReqFlightDTO reqFlightDTO = new ReqFlightDTO();
        reqFlightDTO.setAirlineName("Airline");
        reqFlightDTO.setFlightNumber("123");
        reqFlightDTO.setDestinationAirport("Airport A");
        reqFlightDTO.setAirportName("Airport B");
        reqFlightDTO.setDepartureTime(LocalTime.of(10, 30));

        Flight flight = new Flight();
        flight.setAirlineName(reqFlightDTO.getAirlineName());
        flight.setFlightNumber(reqFlightDTO.getFlightNumber());
        flight.setDestinationAirport(reqFlightDTO.getDestinationAirport());
        flight.setAirportName(reqFlightDTO.getAirportName());
        flight.setDate(LocalDate.now());
        flight.setDepartureTime(reqFlightDTO.getDepartureTime());

        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        ResFlightDTO flightDTO = flightService.createFlight(reqFlightDTO);
        assertNotNull(flightDTO);
        assertEquals(reqFlightDTO.getFlightNumber(), flightDTO.getFlightNumber());
        assertEquals(reqFlightDTO.getAirlineName(), flightDTO.getAirlineName());
    }

    @Test
    void testGetAllFlights_Success() {
        Flight flight = new Flight();
        flight.setAirlineName("Airline");
        flight.setFlightNumber("123");
        flight.setDestinationAirport("Airport A");
        flight.setAirportName("Airport B");
        flight.setDate(LocalDate.now());
        flight.setDepartureTime(LocalTime.of(10, 30));

        List<Flight> flightList = new ArrayList<>();
        flightList.add(flight);

        when(flightRepository.findAll()).thenReturn(flightList);

        List<ResFlightDTO> flightDTOList = flightService.getAllFlights();
        assertNotNull(flightDTOList);
        assertEquals(1, flightDTOList.size());
        assertEquals(flight.getFlightNumber(), flightDTOList.get(0).getFlightNumber());
    }

    @Test
    void testGetFlightById_Found() {
        Flight flight = new Flight();
        flight.setAirlineName("Airline");
        flight.setFlightNumber("123");
        flight.setDestinationAirport("Airport A");
        flight.setAirportName("Airport B");
        flight.setDate(LocalDate.now());
        flight.setDepartureTime(LocalTime.of(10, 30));

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        ResFlightDTO flightDTO = flightService.getFlightById(1L);
        assertNotNull(flightDTO);
        assertEquals(flight.getFlightNumber(), flightDTO.getFlightNumber());
    }

    @Test
    void testGetFlightById_NotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        ResFlightDTO flightDTO = flightService.getFlightById(1L);
        assertNull(flightDTO);
    }

    @Test
    void testUpdateFlight_Success() {
        ReqFlightDTO reqFlightDTO = new ReqFlightDTO();
        reqFlightDTO.setFlightNumber("124");
        reqFlightDTO.setDestinationAirport("Airport B");
        reqFlightDTO.setAirportName("Airport C");
        reqFlightDTO.setDepartureTime(LocalTime.of(15, 45));

        Flight existingFlight = new Flight();
        existingFlight.setFlightNumber("123");
        existingFlight.setDestinationAirport("Airport A");
        existingFlight.setAirportName("Airport B");
        existingFlight.setDate(LocalDate.now());
        existingFlight.setDepartureTime(LocalTime.of(10, 30));

        when(flightRepository.findById(1L)).thenReturn(Optional.of(existingFlight));

        Flight updatedFlight = new Flight();
        updatedFlight.setFlightNumber(reqFlightDTO.getFlightNumber());
        updatedFlight.setDestinationAirport(reqFlightDTO.getDestinationAirport());
        updatedFlight.setAirportName(reqFlightDTO.getAirportName());
        updatedFlight.setDate(LocalDate.now());
        updatedFlight.setDepartureTime(reqFlightDTO.getDepartureTime());

        when(flightRepository.save(any(Flight.class))).thenReturn(updatedFlight);

        ResFlightDTO flightDTO = flightService.updateFlight(1L, reqFlightDTO);
        assertNotNull(flightDTO);
        assertEquals(reqFlightDTO.getFlightNumber(), flightDTO.getFlightNumber());
        assertEquals(reqFlightDTO.getDestinationAirport(), flightDTO.getDestinationAirport());
    }

    @Test
    void testUpdateFlight_NotFound() {
        ReqFlightDTO reqFlightDTO = new ReqFlightDTO();
        reqFlightDTO.setFlightNumber("124");

        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        ResFlightDTO flightDTO = flightService.updateFlight(1L, reqFlightDTO);
        assertNull(flightDTO);
    }

    @Test
    void testDeleteFlight_Success() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(new Flight()));

        ResponseDTO responseDTO = flightService.deleteFlight(1L);
        assertNotNull(responseDTO);
        assertEquals("Flight Deleted Successfully ...", responseDTO.getMessage());

        verify(flightRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteFlight_NotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseDTO responseDTO = flightService.deleteFlight(1L);
        assertNull(responseDTO);
    }
}

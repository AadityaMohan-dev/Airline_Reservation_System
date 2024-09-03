package com.aaditya.airline_reservation_system.Controller;

import com.aaditya.airline_reservation_system.DTO.ReqFlightDTO;
import com.aaditya.airline_reservation_system.DTO.ResFlightDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Services.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightControllerTest {

    @InjectMocks
    private FlightController flightController;

    @Mock
    private FlightService flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFlight_Success() {
        // Arrange
        ReqFlightDTO reqFlightDTO = new ReqFlightDTO();
        ResFlightDTO resFlightDTO = new ResFlightDTO();
        when(flightService.createFlight(reqFlightDTO)).thenReturn(resFlightDTO);

        // Act
        ResponseEntity<Object> response = flightController.createFlight(reqFlightDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(resFlightDTO, response.getBody());
    }

    @Test
    void testCreateFlight_Failure() {
        // Arrange
        ReqFlightDTO reqFlightDTO = new ReqFlightDTO();
        when(flightService.createFlight(reqFlightDTO)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = flightController.createFlight(reqFlightDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("something went wrong...", responseDTO.getMessage());
    }

    @Test
    void testGetAllFlights_Success() {
        // Arrange
        List<ResFlightDTO> flights = new ArrayList<>();
        ResFlightDTO flightDTO = new ResFlightDTO();
        flights.add(flightDTO);
        when(flightService.getAllFlights()).thenReturn(flights);

        // Act
        ResponseEntity<Object> response = flightController.getAllFlights();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
    }

    @Test
    void testGetAllFlights_NoFlights() {
        // Arrange
        when(flightService.getAllFlights()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = flightController.getAllFlights();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("No Flight Assigned.", responseDTO.getMessage());
    }

    @Test
    void testGetFlightById_Success() {
        // Arrange
        Long flightId = 1L;
        ResFlightDTO flightDTO = new ResFlightDTO();
        when(flightService.getFlightById(flightId)).thenReturn(flightDTO);

        // Act
        ResponseEntity<Object> response = flightController.getFlightById(flightId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flightDTO, response.getBody());
    }

    @Test
    void testGetFlightById_Failure() {
        // Arrange
        Long flightId = 1L;
        when(flightService.getFlightById(flightId)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = flightController.getFlightById(flightId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("No Flight Assigned.", responseDTO.getMessage());
    }

    @Test
    void testUpdateFlight_Success() {
        // Arrange
        Long flightId = 1L;
        ReqFlightDTO reqFlightDTO = new ReqFlightDTO();
        ResFlightDTO updatedFlightDTO = new ResFlightDTO();
        when(flightService.updateFlight(flightId, reqFlightDTO)).thenReturn(updatedFlightDTO);

        // Act
        ResponseEntity<Object> response = flightController.updateFlight(flightId, reqFlightDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedFlightDTO, response.getBody());
    }

    @Test
    void testUpdateFlight_Failure() {
        // Arrange
        Long flightId = 1L;
        ReqFlightDTO reqFlightDTO = new ReqFlightDTO();
        when(flightService.updateFlight(flightId, reqFlightDTO)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = flightController.updateFlight(flightId, reqFlightDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteFlight_Success() {
        // Arrange
        Long flightId = 1L;
        ResponseDTO responseDTO = new ResponseDTO("Flight deleted successfully");
        when(flightService.deleteFlight(flightId)).thenReturn(responseDTO);

        // Act
        ResponseEntity<ResponseDTO> response = flightController.deleteFlight(flightId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testDeleteFlight_Failure() {
        // Arrange
        Long flightId = 1L;
        when(flightService.deleteFlight(flightId)).thenReturn(null);

        // Act
        ResponseEntity<ResponseDTO> response = flightController.deleteFlight(flightId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("Flight with id : " + flightId + " Not Found!!", responseDTO.getMessage());
    }
}

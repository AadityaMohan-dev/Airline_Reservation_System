package com.aaditya.airline_reservation_system.Controller;

import com.aaditya.airline_reservation_system.DTO.ReqUserDTO;
import com.aaditya.airline_reservation_system.DTO.ResPassengerDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Services.PassengerService;
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

class PassengerControllerTest {

    @InjectMocks
    private PassengerController passengerController;

    @Mock
    private PassengerService passengerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewPassenger_Success() {
        // Arrange
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        ResponseDTO responseDTO = new ResponseDTO("Passenger added successfully");
        when(passengerService.addNewPassenger(reqUserDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<ResponseDTO> response = passengerController.addNewPassenger(reqUserDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testAddNewPassenger_Failure() {
        // Arrange
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        when(passengerService.addNewPassenger(reqUserDTO)).thenReturn(null);

        // Act
        ResponseEntity<ResponseDTO> response = passengerController.addNewPassenger(reqUserDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ResponseDTO responseDTO = response.getBody();
        assertNotNull(responseDTO);
        assertEquals("Something Went Wrong", responseDTO.getMessage());
    }

    @Test
    void testGetAllPassengerDetails_Success() {
        // Arrange
        List<ResPassengerDTO> passengerDTOs = new ArrayList<>();
        ResPassengerDTO passengerDTO = new ResPassengerDTO();
        passengerDTOs.add(passengerDTO);
        when(passengerService.getAllPassengerDetails()).thenReturn(passengerDTOs);

        // Act
        ResponseEntity<List<ResPassengerDTO>> response = passengerController.getAllPassengerDetails();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passengerDTOs, response.getBody());
    }

//    @Test
//    void testGetAllPassengerDetails_NoContent() {
//        // Arrange
//        when(passengerService.getAllPassengerDetails()).thenReturn(new ArrayList<>());
//
//        // Act
//        ResponseEntity<List<ResPassengerDTO>> response = passengerController.getAllPassengerDetails();
//
//        // Assert
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        assertTrue(response.getBody().isEmpty());
//    }

    @Test
    void testGetPassengerDetails_Success() {
        // Arrange
        Long passengerId = 1L;
        ResPassengerDTO passengerDTO = new ResPassengerDTO();
        when(passengerService.getPassengerById(passengerId)).thenReturn(passengerDTO);

        // Act
        ResponseEntity<?> response = passengerController.getPassengerDetails(passengerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passengerDTO, response.getBody());
    }

    @Test
    void testGetPassengerDetails_NotFound() {
        // Arrange
        Long passengerId = 1L;
        when(passengerService.getPassengerById(passengerId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = passengerController.getPassengerDetails(passengerId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("Passenger Not Found with id : " + passengerId, responseDTO.getMessage());
    }

    @Test
    void testDeleteById_Success() {
        // Arrange
        Long passengerId = 1L;
        ResponseDTO responseDTO = new ResponseDTO("Passenger deleted successfully");
        when(passengerService.deletePassengerById(passengerId)).thenReturn(responseDTO);

        // Act
        ResponseEntity<ResponseDTO> response = passengerController.deleteById(passengerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testDeleteById_NotFound() {
        // Arrange
        Long passengerId = 1L;
        when(passengerService.deletePassengerById(passengerId)).thenReturn(null);

        // Act
        ResponseEntity<ResponseDTO> response = passengerController.deleteById(passengerId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseDTO responseDTO = response.getBody();
        assertNotNull(responseDTO);
        assertEquals("Passenger Not Found with id : " + passengerId, responseDTO.getMessage());
    }

    @Test
    void testUpdatePassengerProfile_Success() {
        // Arrange
        Long passengerId = 1L;
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        ResPassengerDTO updatedPassengerDTO = new ResPassengerDTO();
        when(passengerService.updatePassengerDetails(passengerId, reqUserDTO)).thenReturn(updatedPassengerDTO);

        // Act
        ResponseEntity<Object> response = passengerController.updatePassengerProfile(reqUserDTO, passengerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPassengerDTO, response.getBody());
    }

    @Test
    void testUpdatePassengerProfile_NotFound() {
        // Arrange
        Long passengerId = 1L;
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        when(passengerService.updatePassengerDetails(passengerId, reqUserDTO)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = passengerController.updatePassengerProfile(reqUserDTO, passengerId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("Passenger Not Found with id : " + passengerId, responseDTO.getMessage());
    }
}

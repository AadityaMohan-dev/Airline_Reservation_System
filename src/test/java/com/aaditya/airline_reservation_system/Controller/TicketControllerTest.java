package com.aaditya.airline_reservation_system.Controller;

import com.aaditya.airline_reservation_system.DTO.ReqTicketDTO;
import com.aaditya.airline_reservation_system.DTO.ResTicketDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Services.TicketService;
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

class TicketControllerTest {

    @InjectMocks
    private TicketController ticketController;

    @Mock
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTicket_Success() {
        // Arrange
        ReqTicketDTO reqTicketDTO = new ReqTicketDTO();
        ResTicketDTO resTicketDTO = new ResTicketDTO();
        when(ticketService.createTicket(reqTicketDTO)).thenReturn(resTicketDTO);

        // Act
        ResponseEntity<Object> response = ticketController.createTicket(reqTicketDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(resTicketDTO, response.getBody());
    }

    @Test
    void testCreateTicket_Failure() {
        // Arrange
        ReqTicketDTO reqTicketDTO = new ReqTicketDTO();
        when(ticketService.createTicket(reqTicketDTO)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = ticketController.createTicket(reqTicketDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("Something Went Wrong...", responseDTO.getMessage());
    }

    @Test
    void testGetAllTickets_Success() {
        // Arrange
        List<ResTicketDTO> ticketDTOs = new ArrayList<>();
        ResTicketDTO ticketDTO = new ResTicketDTO();
        ticketDTOs.add(ticketDTO);
        when(ticketService.getAllTickets()).thenReturn(ticketDTOs);

        // Act
        ResponseEntity<Object> response = ticketController.getAllTickets();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketDTOs, response.getBody());
    }

    @Test
    void testGetAllTickets_Failure() {
        // Arrange
        when(ticketService.getAllTickets()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = ticketController.getAllTickets();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("Something Went Wrong...", responseDTO.getMessage());
    }

    @Test
    void testGetTicketById_Success() {
        // Arrange
        Long ticketId = 1L;
        ResTicketDTO ticketDTO = new ResTicketDTO();
        when(ticketService.getTicketById(ticketId)).thenReturn(ticketDTO);

        // Act
        ResponseEntity<Object> response = ticketController.getTicketById(ticketId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketDTO, response.getBody());
    }

    @Test
    void testGetTicketById_Failure() {
        // Arrange
        Long ticketId = 1L;
        when(ticketService.getTicketById(ticketId)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = ticketController.getTicketById(ticketId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("Something Went Wrong...", responseDTO.getMessage());
    }

    @Test
    void testDeleteTicketById_Success() {
        // Arrange
        Long ticketId = 1L;
        ResponseDTO responseDTO = new ResponseDTO("Ticket deleted successfully");
        when(ticketService.deleteTicketById(ticketId)).thenReturn(responseDTO);

        // Act
        ResponseEntity<Object> response = ticketController.deleteTicketById(ticketId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testDeleteTicketById_NotFound() {
        // Arrange
        Long ticketId = 1L;
        when(ticketService.deleteTicketById(ticketId)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = ticketController.deleteTicketById(ticketId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("Ticket with id : " + ticketId + " Not Found !!", responseDTO.getMessage());
    }
}

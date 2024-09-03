package com.aaditya.airline_reservation_system.Controller;

import com.aaditya.airline_reservation_system.DTO.ReqUserDTO;
import com.aaditya.airline_reservation_system.DTO.ResAdminDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Services.AdminService;
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

class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewAdmin_Success() {
        // Arrange
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        ResponseDTO responseDTO = new ResponseDTO("Admin added successfully");
        when(adminService.addNewAdmin(reqUserDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<Object> response = adminController.addNewAdmin(reqUserDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testAddNewAdmin_Failure() {
        // Arrange
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        when(adminService.addNewAdmin(reqUserDTO)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = adminController.addNewAdmin(reqUserDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("Something Went Wrong", responseDTO.getMessage());
    }

    @Test
    void testGetAllAdminDetails_Success() throws Exception {
        // Arrange
        List<ResAdminDTO> adminList = new ArrayList<>();
        ResAdminDTO adminDTO = new ResAdminDTO();
        adminList.add(adminDTO);
        when(adminService.getAllAdminDetails()).thenReturn(adminList);

        // Act
        ResponseEntity<Object> response = adminController.getAllAdminDetails();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(adminList, response.getBody());
    }

    @Test
    void testGetAllAdminDetails_Empty() throws Exception {
        // Arrange
        when(adminService.getAllAdminDetails()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<Object> response = adminController.getAllAdminDetails();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("Something Went Wrong", responseDTO.getMessage());
    }

    @Test
    void testGetAdminDetails_Success() {
        // Arrange
        Long adminId = 1L;
        ResAdminDTO adminDTO = new ResAdminDTO();
        when(adminService.getAdminById(adminId)).thenReturn(adminDTO);

        // Act
        ResponseEntity<Object> response = adminController.getAdminDetails(adminId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(adminDTO, response.getBody());
    }

    @Test
    void testGetAdminDetails_Failure() {
        // Arrange
        Long adminId = 1L;
        when(adminService.getAdminById(adminId)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = adminController.getAdminDetails(adminId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("Admin Not Found with id : " + adminId, responseDTO.getMessage());
    }

    @Test
    void testDeleteById_Success() {
        // Arrange
        Long adminId = 1L;
        ResponseDTO responseDTO = new ResponseDTO("Admin deleted successfully");
        when(adminService.deleteAdminById(adminId)).thenReturn(responseDTO);

        // Act
        ResponseEntity<Object> response = adminController.deleteById(adminId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testDeleteById_Failure() {
        // Arrange
        Long adminId = 1L;
        when(adminService.deleteAdminById(adminId)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = adminController.deleteById(adminId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("Admin Not Found with id : " + adminId, responseDTO.getMessage());
    }

    @Test
    void testUpdateAdminProfile_Success() {
        // Arrange
        Long adminId = 1L;
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        ResAdminDTO adminDTO = new ResAdminDTO();
        when(adminService.updateAdminDetails(adminId, reqUserDTO)).thenReturn(adminDTO);

        // Act
        ResponseEntity<Object> response = adminController.updateAdminProfile(reqUserDTO, adminId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(adminDTO, response.getBody());
    }

    @Test
    void testUpdateAdminProfile_Failure() {
        // Arrange
        Long adminId = 1L;
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        when(adminService.updateAdminDetails(adminId, reqUserDTO)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = adminController.updateAdminProfile(reqUserDTO, adminId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("Admin Not Found with id : " + adminId, responseDTO.getMessage());
    }
}

package com.aaditya.airline_reservation_system.servicesTest;

import com.aaditya.airline_reservation_system.DTO.ReqUserDTO;
import com.aaditya.airline_reservation_system.DTO.ResPassengerDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Entity.Passenger;
import com.aaditya.airline_reservation_system.Entity.User;
import com.aaditya.airline_reservation_system.Enums.RoleEnum;
import com.aaditya.airline_reservation_system.Repository.PassengerRepository;
import com.aaditya.airline_reservation_system.Repository.UserRepository;
import com.aaditya.airline_reservation_system.Services.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PassengerService passengerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewPassenger_Success() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("john_doe");
        reqUserDTO.setPassword("password123");
        reqUserDTO.setRePassword("password123");
        reqUserDTO.setEmail("john@example.com");
        reqUserDTO.setPhone("1234567890");

        User user = new User();
        user.setId(1L);
        user.setUsername(reqUserDTO.getUsername());
        user.setPassword(reqUserDTO.getPassword());
        user.setRole(RoleEnum.PASSENGER);

        Passenger passenger = new Passenger();
        passenger.setPassenger_id(user.getId());
        passenger.setEmail(reqUserDTO.getEmail());
        passenger.setUser(user);
        passenger.setPhone(reqUserDTO.getPhone());

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        ResponseDTO responseDTO = passengerService.addNewPassenger(reqUserDTO);
        assertNotNull(responseDTO);
        assertEquals("New Admin Added Successfully.", responseDTO.getMessage());
    }

    @Test
    void testGetAllPassengerDetails_Success() {
        Passenger passenger = new Passenger();
        passenger.setPassenger_id(1L);
        passenger.setEmail("john@example.com");
        passenger.setPhone("1234567890");
        passenger.setUser(new User());
        passenger.getUser().setUsername("john_doe");

        List<Passenger> passengerList = new ArrayList<>();
        passengerList.add(passenger);

        when(passengerRepository.findAll()).thenReturn(passengerList);

        List<ResPassengerDTO> responseList = passengerService.getAllPassengerDetails();
        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals(passenger.getPassenger_id(), responseList.get(0).getId());
    }

    @Test
    void testGetPassengerById_Found() {
        Passenger passenger = new Passenger();
        passenger.setPassenger_id(1L);
        passenger.setEmail("john@example.com");
        passenger.setPhone("1234567890");
        passenger.setUser(new User());
        passenger.getUser().setUsername("john_doe");

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        ResPassengerDTO passengerDTO = passengerService.getPassengerById(1L);
        assertNotNull(passengerDTO);
        assertEquals(passenger.getPassenger_id(), passengerDTO.getId());
    }

    @Test
    void testGetPassengerById_NotFound() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        ResPassengerDTO passengerDTO = passengerService.getPassengerById(1L);
        assertNull(passengerDTO);
    }

    @Test
    void testDeletePassengerById_Success() {
        Passenger passenger = new Passenger();
        passenger.setPassenger_id(1L);
        User user = new User();
        user.setId(2L);
        passenger.setUser(user);

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        doNothing().when(userRepository).deleteById(2L);
        doNothing().when(passengerRepository).deleteById(1L);

        ResponseDTO responseDTO = passengerService.deletePassengerById(1L);
        assertNotNull(responseDTO);
        assertEquals("Passenger with id : 1 Deleted Successfully.", responseDTO.getMessage());
    }

    @Test
    void testDeletePassengerById_NotFound() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseDTO responseDTO = passengerService.deletePassengerById(1L);
        assertNull(responseDTO);
    }

    @Test
    void testUpdatePassengerDetails_Success() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("john_doe_updated");
        reqUserDTO.setPassword("newpassword123");
        reqUserDTO.setRePassword("newpassword123");
        reqUserDTO.setEmail("john_new@example.com");
        reqUserDTO.setPhone("0987654321");

        Passenger existingPassenger = new Passenger();
        existingPassenger.setPassenger_id(1L);
        existingPassenger.setEmail("john@example.com");
        existingPassenger.setPhone("1234567890");
        User existingUser = new User();
        existingUser.setId(2L);
        existingUser.setUsername("john_doe");
        existingUser.setPassword("password123");
        existingUser.setRole(RoleEnum.PASSENGER);
        existingPassenger.setUser(existingUser);

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existingPassenger));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(existingPassenger);

        ResPassengerDTO passengerDTO = passengerService.updatePassengerDetails(1L, reqUserDTO);
        assertNotNull(passengerDTO);
        assertEquals(reqUserDTO.getUsername(), passengerDTO.getUsername());
        assertEquals(reqUserDTO.getEmail(), passengerDTO.getEmail());
        assertEquals(reqUserDTO.getPhone(), passengerDTO.getPhone());
    }

    @Test
    void testUpdatePassengerDetails_NotFound() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("john_doe_updated");

        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        ResPassengerDTO passengerDTO = passengerService.updatePassengerDetails(1L, reqUserDTO);
        assertNull(passengerDTO);
    }
}

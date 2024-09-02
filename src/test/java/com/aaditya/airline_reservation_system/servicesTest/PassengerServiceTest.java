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
import static org.mockito.Mockito.*;

public class PassengerServiceTest {

    @InjectMocks
    private PassengerService passengerService;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewPassenger_Success() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("john_doe");
        reqUserDTO.setEmail("john@example.com");
        reqUserDTO.setPhone("1234567890");

        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setRole(RoleEnum.PASSENGER);

        Passenger passenger = new Passenger();
        passenger.setPassenger_id(1L);
        passenger.setEmail("john@example.com");
        passenger.setPhone("1234567890");
        passenger.setUser(user);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        ResponseDTO responseDTO = passengerService.addNewPassenger(reqUserDTO);

        assertNotNull(responseDTO);
        assertEquals("New Admin Added Successfully.", responseDTO.getMessage());
    }

    @Test
    public void testGetAllPassengerDetails_Success() {
        Passenger passenger1 = new Passenger();
        passenger1.setPassenger_id(1L);
        passenger1.setEmail("john@example.com");
        passenger1.setPhone("1234567890");
        User user1 = new User();
        user1.setUsername("john_doe");
        passenger1.setUser(user1);

        Passenger passenger2 = new Passenger();
        passenger2.setPassenger_id(2L);
        passenger2.setEmail("jane@example.com");
        passenger2.setPhone("0987654321");
        User user2 = new User();
        user2.setUsername("jane_doe");
        passenger2.setUser(user2);

        List<Passenger> passengers = List.of(passenger1, passenger2);

        when(passengerRepository.findAll()).thenReturn(passengers);

        List<ResPassengerDTO> responseList = passengerService.getAllPassengerDetails();

        assertNotNull(responseList);
        assertEquals(2, responseList.size());
        assertEquals("john_doe", responseList.get(0).getUsername());
        assertEquals("jane_doe", responseList.get(1).getUsername());
    }

    @Test
    public void testGetAllPassengerDetails_Empty() {
        when(passengerRepository.findAll()).thenReturn(new ArrayList<>());

        List<ResPassengerDTO> responseList = passengerService.getAllPassengerDetails();

        assertNull(responseList);
    }

    @Test
    public void testGetPassengerById_Found() {
        Passenger passenger = new Passenger();
        passenger.setPassenger_id(1L);
        passenger.setEmail("john@example.com");
        passenger.setPhone("1234567890");
        User user = new User();
        user.setUsername("john_doe");
        passenger.setUser(user);

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        ResPassengerDTO responseDTO = passengerService.getPassengerById(1L);

        assertNotNull(responseDTO);
        assertEquals("john_doe", responseDTO.getUsername());
        assertEquals("john@example.com", responseDTO.getEmail());
    }

    @Test
    public void testGetPassengerById_NotFound() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        ResPassengerDTO responseDTO = passengerService.getPassengerById(1L);

        assertNull(responseDTO);
    }

    @Test
    public void testDeletePassengerById_Success() {
        Passenger passenger = new Passenger();
        passenger.setPassenger_id(1L);
        User user = new User();
        user.setId(1L);
        passenger.setUser(user);

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        ResponseDTO responseDTO = passengerService.deletePassengerById(1L);

        assertNotNull(responseDTO);
        assertEquals("Passenger with id : 1 Deleted Successfully.", responseDTO.getMessage());
        verify(userRepository, times(1)).deleteById(1L);
        verify(passengerRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePassengerById_NotFound() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseDTO responseDTO = passengerService.deletePassengerById(1L);

        assertNull(responseDTO);
        verify(userRepository, never()).deleteById(anyLong());
        verify(passengerRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testUpdatePassengerDetails_Success() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("john_doe_updated");
        reqUserDTO.setEmail("john_updated@example.com");
        reqUserDTO.setPhone("9876543210");

        Passenger existingPassenger = new Passenger();
        existingPassenger.setPassenger_id(1L);
        existingPassenger.setEmail("john@example.com");
        existingPassenger.setPhone("1234567890");
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("john_doe");
        existingUser.setRole(RoleEnum.PASSENGER);
        existingPassenger.setUser(existingUser);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("john_doe_updated");
        updatedUser.setRole(RoleEnum.PASSENGER);

        Passenger updatedPassenger = new Passenger();
        updatedPassenger.setPassenger_id(1L);
        updatedPassenger.setEmail("john_updated@example.com");
        updatedPassenger.setPhone("9876543210");
        updatedPassenger.setUser(updatedUser);

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existingPassenger));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(updatedPassenger);

        ResPassengerDTO responseDTO = passengerService.updatePassengerDetails(1L, reqUserDTO);

        assertNotNull(responseDTO);
        assertEquals("john_doe_updated", responseDTO.getUsername());
        assertEquals("john_updated@example.com", responseDTO.getEmail());
        assertEquals("9876543210", responseDTO.getPhone());
    }

    @Test
    public void testUpdatePassengerDetails_NotFound() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("john_doe_updated");
        reqUserDTO.setEmail("john_updated@example.com");
        reqUserDTO.setPhone("9876543210");

        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        ResPassengerDTO responseDTO = passengerService.updatePassengerDetails(1L, reqUserDTO);

        assertNull(responseDTO);
        verify(userRepository, never()).save(any(User.class));
        verify(passengerRepository, never()).save(any(Passenger.class));
    }
}

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PassengerService passengerService;

    @Test
    public void testAddNewPassengerSuccess() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("passengerUser");
        reqUserDTO.setPassword("passengerPass");
        reqUserDTO.setRePassword("passengerPass");
        reqUserDTO.setEmail("passenger@example.com");
        reqUserDTO.setPhone("1234567890");

        User user = new User();
        user.setId(1L);
        user.setUsername("passengerUser");
        user.setPassword("passengerPass");
        user.setRole(RoleEnum.ADMIN);

        Passenger passenger = new Passenger();
        passenger.setEmail("passenger@example.com");
        passenger.setUser(user);
        passenger.setPhone("1234567890");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        ResponseDTO responseDTO = passengerService.addNewPassenger(reqUserDTO);

        assertNull(responseDTO);
        verify(userRepository, times(1)).save(any(User.class));
        verify(passengerRepository, times(1)).save(any(Passenger.class));
    }

    @Test
    public void testAddNewPassengerPasswordMismatch() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("passengerUser");
        reqUserDTO.setPassword("passengerPass");
        reqUserDTO.setRePassword("differentPass");
        reqUserDTO.setEmail("passenger@example.com");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> passengerService.addNewPassenger(reqUserDTO)
        );
        assertEquals("Passwords do not match", thrown.getMessage());
    }

    @Test
    public void testGetAllPassengerDetailsSuccess() {
        List<Passenger> passengerList = new ArrayList<>();
        Passenger passenger = new Passenger();
        passenger.setEmail("passenger@example.com");
        passenger.setPhone("1234567890");
        passenger.setUser(new User());
        passenger.getUser().setUsername("passengerUser");
        passengerList.add(passenger);

        when(passengerRepository.findAll()).thenReturn(passengerList);

        List<ResPassengerDTO> responseList = passengerService.getAllPassengerDetails();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals("passengerUser", responseList.get(0).getUsername());
    }

    @Test
    public void testGetAllPassengerDetailsFailure() {
        when(passengerRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> passengerService.getAllPassengerDetails()
        );
        assertEquals("Failed to retrieve passenger details: Database error", thrown.getMessage());
    }

    @Test
    public void testGetPassengerByIdSuccess() {
        Long id = 1L;
        Passenger passenger = new Passenger();
        passenger.setEmail("passenger@example.com");
        passenger.setPhone("1234567890");
        passenger.setUser(new User());
        passenger.getUser().setUsername("passengerUser");

        when(passengerRepository.findById(id)).thenReturn(Optional.of(passenger));

        ResPassengerDTO passengerDTO = passengerService.getPassengerById(id);

        assertNotNull(passengerDTO);
        assertEquals("passengerUser", passengerDTO.getUsername());
        assertEquals("passenger@example.com", passengerDTO.getEmail());
        assertEquals("1234567890", passengerDTO.getPhone());
    }

    @Test
    public void testGetPassengerByIdNotFound() {
        Long id = 1L;

        when(passengerRepository.findById(id)).thenReturn(Optional.empty());

        ResPassengerDTO passengerDTO = passengerService.getPassengerById(id);

        assertNull(passengerDTO);
    }

    @Test
    public void testDeletePassengerByIdSuccess() {
        Long id = 1L;
        Passenger passenger = new Passenger();

        when(passengerRepository.findById(id)).thenReturn(Optional.of(passenger));

        ResponseDTO responseDTO = passengerService.deletePassengerById(id);

        assertEquals("Passenger with id : " + id + " Deleted Successfully.", responseDTO.getMessage());
        verify(userRepository, times(1)).deleteById(id);
        verify(passengerRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeletePassengerByIdNotFound() {
        Long id = 1L;

        when(passengerRepository.findById(id)).thenReturn(Optional.empty());

        ResponseDTO responseDTO = passengerService.deletePassengerById(id);

        assertNull(responseDTO);
    }

    @Test
    public void testUpdatePassengerDetailsSuccess() {
        Long id = 1L;
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("updatedUser");
        reqUserDTO.setPassword("newPass");
        reqUserDTO.setRePassword("newPass");
        reqUserDTO.setEmail("updated@example.com");
        reqUserDTO.setPhone("0987654321");

        Passenger passenger = new Passenger();
        passenger.setPassenger_id(id);
        passenger.setEmail("updated@example.com");
        passenger.setPhone("0987654321");
        passenger.setUser(new User());

        User user = new User();
        user.setId(id);
        user.setUsername("updatedUser");
        user.setPassword("newPass");
        user.setRole(RoleEnum.ADMIN);

        when(passengerRepository.findById(id)).thenReturn(Optional.of(passenger));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        ResPassengerDTO passengerDTO = passengerService.updatePassengerDetails(id, reqUserDTO);

        assertNotNull(passengerDTO);
        assertEquals("updated@example.com", passengerDTO.getEmail());
        assertEquals(id, passengerDTO.getId());
        assertEquals("updatedUser", passengerDTO.getUsername());
    }

    @Test
    public void testUpdatePassengerDetailsPasswordMismatch() {
        Long id = 1L;
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("updatedUser");
        reqUserDTO.setPassword("newPass");
        reqUserDTO.setRePassword("differentPass");
        reqUserDTO.setEmail("updated@example.com");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> passengerService.updatePassengerDetails(id, reqUserDTO)
        );
        assertEquals("Passwords do not match", thrown.getMessage());
    }

    @Test
    public void testUpdatePassengerDetailsNotFound() {
        Long id = 1L;
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("updatedUser");
        reqUserDTO.setPassword("newPass");
        reqUserDTO.setRePassword("newPass");
        reqUserDTO.setEmail("updated@example.com");

        when(passengerRepository.findById(id)).thenReturn(Optional.empty());

        ResPassengerDTO passengerDTO = passengerService.updatePassengerDetails(id, reqUserDTO);

        assertNull(passengerDTO);
    }
}

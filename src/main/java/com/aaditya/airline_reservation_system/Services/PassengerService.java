package com.aaditya.airline_reservation_system.Services;

import com.aaditya.airline_reservation_system.DTO.ReqUserDTO;
import com.aaditya.airline_reservation_system.DTO.ResPassengerDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Entity.Passenger;
import com.aaditya.airline_reservation_system.Entity.User;
import com.aaditya.airline_reservation_system.Enums.RoleEnum;
import com.aaditya.airline_reservation_system.Repository.PassengerRepository;
import com.aaditya.airline_reservation_system.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {

    @Autowired
    PassengerRepository passengerRepository;
    @Autowired
    UserRepository userRepository;
    public ResponseDTO addNewPassenger(ReqUserDTO reqUserDTO) {
        try {
            User user = new User();
            user.setUsername(reqUserDTO.getUsername());
            if (reqUserDTO.getPassword().equals(reqUserDTO.getRePassword())) {
                user.setPassword(reqUserDTO.getPassword());
            } else {
                throw new IllegalArgumentException("Passwords do not match");
            }
            user.setRole(RoleEnum.ADMIN);
            userRepository.save(user);
            Passenger passenger = new Passenger();
            passenger.setEmail(reqUserDTO.getEmail());
            passenger.setUser(user);
            passenger.setPhone(reqUserDTO.getPhone());
            passengerRepository.save(passenger);
        }catch (Exception e){
            throw new RuntimeException("Something Went Wrong" + e);
        }
        return null;
    }

    public List<ResPassengerDTO> getAllPassengerDetails() {
        List<Passenger> passengerList = passengerRepository.findAll();
        List<ResPassengerDTO> responseList = new ArrayList<>();
        try{
            for (Passenger passenger : passengerList){
                ResPassengerDTO passengerDTO = new ResPassengerDTO();
                passengerDTO.setEmail(passenger.getEmail());
                passengerDTO.setPhone(passenger.getPhone());
                passengerDTO.setRole("PASSENGER");
                passengerDTO.setUsername(passenger.getUser().getUsername());
            }
        }catch(Exception e){
            throw new RuntimeException("Failed to retrieve passenger details: " + e.getMessage(), e);
        }
        return null;
    }

    public ResPassengerDTO getPassengerById(Long id) {
        Optional<Passenger> passenger = passengerRepository.findById(id);

        try {
            if(passenger.isPresent()){
                Passenger passenger_data = passenger.get();
                ResPassengerDTO passengerDTO = new ResPassengerDTO();
                passengerDTO.setUsername(passenger_data.getUser().getUsername());
                passengerDTO.setPhone(passenger_data.getPhone());
                passengerDTO.setRole("PASSENGER");
                passengerDTO.setEmail(passenger_data.getEmail());
                return passengerDTO;

            }
        }catch(Exception e){
            throw new RuntimeException("User not Found with id :" + id + e.getStackTrace() + e);
        }
        return null;
    }

    public ResponseDTO deletePassengerById(Long id) {
        Optional<Passenger> passenger = passengerRepository.findById(id);
        if(passenger.isPresent()){
            userRepository.deleteById(id);
            passengerRepository.deleteById(id);
            return new ResponseDTO("Passenger with id : " + id +" Deleted Successfully.");
        }
        return null;
    }

    public ResPassengerDTO updatePassengerDetails(Long id, ReqUserDTO reqUserDTO) {
        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if(optionalPassenger.isEmpty()){
            return null;
        }
        Passenger passenger = optionalPassenger.get();
        User user = new User();
        user.setId(id);
        user.setUsername(reqUserDTO.getUsername());
        user.setRole(RoleEnum.PASSENGER);
        if(reqUserDTO.getPassword().equals(reqUserDTO.getRePassword())) {
            user.setPassword(reqUserDTO.getPassword());
        }else {
            throw new IllegalArgumentException("Passwords do not match");
        }
        Passenger updatedPassenger = new Passenger();
        updatedPassenger.setPassenger_id(id);
        updatedPassenger.setUser(user);
        updatedPassenger.setEmail(reqUserDTO.getEmail());
        try {
            userRepository.save(user);
            passengerRepository.save(passenger);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while saving user or passenger: " + e.getMessage(), e);
        }

        ResPassengerDTO passengerDTO = new ResPassengerDTO();
        passengerDTO.setEmail(updatedPassenger.getEmail());
        passengerDTO.setId(updatedPassenger.getPassenger_id());
        passengerDTO.setRole("PASSENGER");
        passengerDTO.setUsername(user.getUsername());

        return passengerDTO;
    }
}
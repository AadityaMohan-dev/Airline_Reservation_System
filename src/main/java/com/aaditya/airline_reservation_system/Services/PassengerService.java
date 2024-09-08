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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    PasswordEncoder passwordEncoder;
    public ResponseDTO addNewPassenger(ReqUserDTO reqUserDTO) {
        try{
            User user = new User();
            user.setUsername(reqUserDTO.getUsername());
            user.setPassword(passwordEncoder.encode(reqUserDTO.getPassword()));
            user.setRole(RoleEnum.PASSENGER);
            userRepository.save(user);
            Passenger passenger = new Passenger();
            passenger.setPassenger_id(user.getId());
            passenger.setEmail(reqUserDTO.getEmail());
            passenger.setUser(user);
            passenger.setPhone(reqUserDTO.getPhone());
            passengerRepository.save(passenger);
            ResponseDTO response = new ResponseDTO();
            response.setMessage("New Admin Added Successfully.");
            return response;
        }catch (Exception e){
            throw new RuntimeException("Something Went Wrong" + e);
        }

    }

    public List<ResPassengerDTO> getAllPassengerDetails() {
        List<Passenger> passengerList = passengerRepository.findAll();
        List<ResPassengerDTO> responseList = new ArrayList<>();
        if (passengerList.isEmpty()) return null;
        try{
            for (Passenger passenger : passengerList){
                ResPassengerDTO passengerDTO = new ResPassengerDTO();
                passengerDTO.setId(passenger.getPassenger_id());
                passengerDTO.setEmail(passenger.getEmail());
                passengerDTO.setPhone(passenger.getPhone());
                passengerDTO.setRole("PASSENGER");
                passengerDTO.setUsername(passenger.getUser().getUsername());
                responseList.add(passengerDTO);
            }
        }catch(Exception e){
            throw new RuntimeException("Failed to retrieve passenger details: " + e.getMessage(), e);
        }
        return responseList;
    }

    public ResPassengerDTO getPassengerById(Long id) {
        Optional<Passenger> passenger = passengerRepository.findById(id);
        if(passenger.isEmpty()) return null;
        try {
                Passenger passenger_data = passenger.get();
                ResPassengerDTO passengerDTO = new ResPassengerDTO();
                passengerDTO.setId(passenger_data.getPassenger_id());
                passengerDTO.setUsername(passenger_data.getUser().getUsername());
                passengerDTO.setPhone(passenger_data.getPhone());
                passengerDTO.setRole("PASSENGER");
                passengerDTO.setEmail(passenger_data.getEmail());
                return passengerDTO;
        }catch(Exception e){
            throw new RuntimeException("User not Found with id :" + id + e.getStackTrace() + e);
        }
    }

    public ResponseDTO deletePassengerById(Long id) {
        Optional<Passenger> passenger = passengerRepository.findById(id);
        if(passenger.isPresent()){
            userRepository.deleteById(passenger.get().getUser().getId());
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
        try{
            Passenger passenger = optionalPassenger.get();
            User user = new User();
            user.setId(passenger.getUser().getId());
            user.setUsername(reqUserDTO.getUsername());
            user.setRole(passenger.getUser().getRole());
            user.setPassword(reqUserDTO.getPassword());

            userRepository.save(user);
            Passenger updatedPassenger = new Passenger();
            updatedPassenger.setPassenger_id(passenger.getPassenger_id());
            updatedPassenger.setUser(user);
            updatedPassenger.setEmail(reqUserDTO.getEmail());
            updatedPassenger.setPhone(reqUserDTO.getPhone());

            passengerRepository.save(updatedPassenger);

            ResPassengerDTO passengerDTO = new ResPassengerDTO();
            passengerDTO.setEmail(updatedPassenger.getEmail());
            passengerDTO.setId(updatedPassenger.getPassenger_id());
            passengerDTO.setRole("PASSENGER");
            passengerDTO.setUsername(user.getUsername());
            passengerDTO.setPhone(updatedPassenger.getPhone());

            return passengerDTO;
        }catch (Exception e) {
            throw new RuntimeException("something went wrong "+ e.getStackTrace()+e);
        }
    }
}
package com.aaditya.airline_reservation_system.Controller;

import com.aaditya.airline_reservation_system.DTO.ReqUserDTO;
import com.aaditya.airline_reservation_system.DTO.ResAdminDTO;
import com.aaditya.airline_reservation_system.DTO.ResPassengerDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Services.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @PostMapping
    public ResponseEntity<ResponseDTO> addNewPassenger(@RequestBody ReqUserDTO reqUserDTO) {
        ResponseDTO response = passengerService.addNewPassenger(reqUserDTO);
        if (response == null) {
            return new ResponseEntity<>(new ResponseDTO("Something Went Wrong"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ResPassengerDTO>> getAllPassengerDetails() {
        List<ResPassengerDTO> passengerDTO = passengerService.getAllPassengerDetails();
        if (passengerDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(passengerDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPassengerDetails(@PathVariable Long id) {
        ResPassengerDTO passenger = passengerService.getPassengerById(id);
        if (passenger == null) {
            return new ResponseEntity<>(new ResponseDTO("Passenger Not Found with id : " + id), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(passenger, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable Long id) {
        ResponseDTO response = passengerService.deletePassengerById(id);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseDTO("Passenger Not Found with id : " + id), HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePassengerProfile(@RequestBody ReqUserDTO reqUserDTO, @PathVariable Long id) {
        ResPassengerDTO passengerDetails = passengerService.updatePassengerDetails(id, reqUserDTO);
        if (passengerDetails != null) {
            return new ResponseEntity<>(passengerDetails, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseDTO("Passenger Not Found with id : " + id), HttpStatus.NOT_FOUND);
    }
}

package com.aaditya.airline_reservation_system.Controller;

import com.aaditya.airline_reservation_system.DTO.ReqFlightDTO;
import com.aaditya.airline_reservation_system.DTO.ResFlightDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;

import com.aaditya.airline_reservation_system.Services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/flight")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @PostMapping
    ResponseEntity<Object> createFlight(@RequestBody ReqFlightDTO reqFlightDTO) {
        ResFlightDTO createdFlight = flightService.createFlight(reqFlightDTO);
        if(createdFlight == null) return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(new ResponseDTO("something went wrong..."));
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(createdFlight);
    }

    @GetMapping
    ResponseEntity<Object> getAllFlights() {
        List<ResFlightDTO> flights = flightService.getAllFlights();
        if(flights == null) return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new ResponseDTO("No Flight Assigned."));
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(flights);

    }

    @GetMapping("/{id}")
    ResponseEntity<Object> getFlightById(@PathVariable Long id) {
        ResFlightDTO flight = flightService.getFlightById(id);
        if(flight == null)return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new ResponseDTO("No Flight Assigned."));
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(flight);
    }

    @PutMapping("/{id}")
    ResponseEntity<Object> updateFlight(@PathVariable Long id, @RequestBody ReqFlightDTO reqFlightDTO) {
        ResFlightDTO updatedFlight = flightService.updateFlight(id, reqFlightDTO);
        return updatedFlight != null ? ResponseEntity.status(HttpStatusCode.valueOf(200)).body(updatedFlight) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseDTO> deleteFlight(@PathVariable Long id) {
        ResponseDTO response = flightService.deleteFlight(id);
        if(response == null) return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new ResponseDTO("Flight with id : "+id+" Not Found!!"));
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(response);
    }
}

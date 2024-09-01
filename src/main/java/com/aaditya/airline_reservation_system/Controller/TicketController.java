package com.aaditya.airline_reservation_system.Controller;

import com.aaditya.airline_reservation_system.DTO.ReqTicketDTO;
import com.aaditya.airline_reservation_system.DTO.ResTicketDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("/{passenger_id}/{flight_id}")
    private ResponseEntity<Object> createTicket(@RequestBody ReqTicketDTO reqTicketDTO , @PathVariable Long passenger_id, @PathVariable Long flight_id){
        ResTicketDTO ticket = ticketService.createTicket(reqTicketDTO, flight_id,passenger_id);
        if(ticket == null){
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(new ResponseDTO("Something Went Wrong..."));
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(ticket);

    }
    @GetMapping
    private ResponseEntity<Object> getAllTickets(){
        List<ResTicketDTO> ticket = ticketService.getAllTickets();
        if(ticket == null){
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(new ResponseDTO("Something Went Wrong..."));
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(ticket);
    }
    @GetMapping("/{id}")
    private ResponseEntity<Object> getTicketById(@PathVariable Long id){
        ResTicketDTO ticket = ticketService.getTicketById(id);
        if(ticket == null){
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(new ResponseDTO("Something Went Wrong..."));
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(ticket);
    }
    @DeleteMapping("/{id}")
    private ResponseEntity<Object> deleteTicketById(@PathVariable Long id){
        ResponseDTO ticket = ticketService.deleteTicketById(id);
        if(ticket == null){
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(new ResponseDTO("Something Went Wrong..."));
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(ticket);
    }
}

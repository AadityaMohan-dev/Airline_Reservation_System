package com.aaditya.airline_reservation_system.Services;

import com.aaditya.airline_reservation_system.DTO.ReqTicketDTO;
import com.aaditya.airline_reservation_system.DTO.ResTicketDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Entity.Flight;
import com.aaditya.airline_reservation_system.Entity.Passenger;
import com.aaditya.airline_reservation_system.Entity.Ticket;
import com.aaditya.airline_reservation_system.Repository.FlightRepository;
import com.aaditya.airline_reservation_system.Repository.PassengerRepository;
import com.aaditya.airline_reservation_system.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private FlightRepository flightRepository;

    private String createTicketNumber(Passenger passenger){

        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        String passengerId = String.valueOf(passenger.getPassenger_id());

        String ticketNumber = passengerId + "-" + String.format("%04d", randomNumber);

        return ticketNumber;
    }
    public ResTicketDTO createTicket(ReqTicketDTO reqTicketDTO, Long flight_id, Long passenger_id) {
        try{
            Optional<Passenger> passengerOptional = passengerRepository.findById(passenger_id);
            Optional<Flight> flightOptional = flightRepository.findById(flight_id);
            if(passengerOptional.isPresent() || flightOptional.isEmpty()) return null;

            Passenger passenger = passengerOptional.get();
            Flight flight =flightOptional.get();

            Ticket ticket = new Ticket();
            ticket.setTicket_number(createTicketNumber(passenger));
            ticket.setFlight(flight);
            ticket.setPassenger(passenger);
            ticketRepository.save(ticket);

            ResTicketDTO ticketDTO = new ResTicketDTO();
            ticketDTO.setTicket_number(ticket.getTicket_number());
            ticketDTO.setUsername(ticket.getPassenger().getUser().getUsername());
            ticketDTO.setAirline_name(ticket.getFlight().getAirlineName());
            ticketDTO.setDate(LocalDate.now());
            ticketDTO.setAirport_name(ticket.getFlight().getAirportName());
            ticketDTO.setDeparture_time(ticket.getFlight().getDepartureTime());
            ticketDTO.setDestination_airport(ticket.getFlight().getDestinationAirport());
            ticketDTO.setFlightNumber(ticket.getFlight().getFlightNumber());

            return  ticketDTO;
        }catch (Exception e){
            throw new RuntimeException("something went wrong");
        }
    }


    public ResponseDTO deleteTicketById(Long id) {
        try{
            Optional<Ticket> optionalTicket = ticketRepository.findById(id);
            if(optionalTicket.isPresent()){
                ticketRepository.deleteById(id);
                return new ResponseDTO("ticket deleted with id : " + id);
            }
        }catch (Exception e){
            throw new RuntimeException("ticket with id " + id + "Not Found !!");
        }
        return null;
    }

    public List<ResTicketDTO> getAllTickets() {
        try{
            List<Ticket> ticketList = ticketRepository.findAll();
            if (ticketList.isEmpty()) return null;
            List<ResTicketDTO> resTicketDTOList = new ArrayList<>();
            for(Ticket ticket : ticketList){
                ResTicketDTO ticketDTO = new ResTicketDTO();
                ticketDTO.setTicket_number(ticket.getTicket_number());
                ticketDTO.setUsername(ticket.getPassenger().getUser().getUsername());
                ticketDTO.setAirline_name(ticket.getFlight().getAirlineName());
                ticketDTO.setDate(LocalDate.now());
                ticketDTO.setAirport_name(ticket.getFlight().getAirportName());
                ticketDTO.setDeparture_time(ticket.getFlight().getDepartureTime());
                ticketDTO.setDestination_airport(ticket.getFlight().getDestinationAirport());
                ticketDTO.setFlightNumber(ticket.getFlight().getFlightNumber());

                resTicketDTOList.add(ticketDTO);
            }
            return resTicketDTOList;
        }catch (Exception e){
            throw new RuntimeException("something went wrong");
        }
    }

    public ResTicketDTO getTicketById(Long id) {
        try{
            Optional<Ticket> ticketOptional = ticketRepository.findById(id);
            if(ticketOptional.isEmpty()) return null;
            Ticket ticket = new Ticket();
            ResTicketDTO ticketDTO = new ResTicketDTO();
            ticketDTO.setTicket_number(ticket.getTicket_number());
            ticketDTO.setUsername(ticket.getPassenger().getUser().getUsername());
            ticketDTO.setAirline_name(ticket.getFlight().getAirlineName());
            ticketDTO.setDate(LocalDate.now());
            ticketDTO.setAirport_name(ticket.getFlight().getAirportName());
            ticketDTO.setDeparture_time(ticket.getFlight().getDepartureTime());
            ticketDTO.setDestination_airport(ticket.getFlight().getDestinationAirport());
            ticketDTO.setFlightNumber(ticket.getFlight().getFlightNumber());
            return ticketDTO;
        }catch (Exception e){
            throw new RuntimeException("ticket with id " + id + "Not Found !!");
        }
    }

}

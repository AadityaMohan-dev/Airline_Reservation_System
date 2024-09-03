package com.aaditya.airline_reservation_system.Services;


import com.aaditya.airline_reservation_system.DTO.ReqFlightDTO;
import com.aaditya.airline_reservation_system.DTO.ResFlightDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Entity.Flight;
import com.aaditya.airline_reservation_system.Repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private TicketService ticketService;
    public ResFlightDTO createFlight(ReqFlightDTO reqFlightDTO) {
        try{
            Flight flight = new Flight();
            flight.setAirlineName(reqFlightDTO.getAirlineName());
            flight.setFlightNumber(reqFlightDTO.getFlightNumber());
            flight.setDestinationAirport(reqFlightDTO.getDestinationAirport());
            flight.setAirportName(reqFlightDTO.getAirportName());
            flight.setDate(LocalDate.now());
            flight.setDepartureTime(reqFlightDTO.getDepartureTime());
            flightRepository.save(flight);

            ResFlightDTO flightDTO = new ResFlightDTO();
            flightDTO.setId(flight.getFlight_id());
            flightDTO.setFlightNumber(flight.getFlightNumber());
            flightDTO.setDate(flight.getDate());
            flightDTO.setAirlineName(flight.getAirlineName());
            flightDTO.setAirportName(flight.getAirportName());
            flightDTO.setDepartureTime(flight.getDepartureTime());
            flightDTO.setDestinationAirport(flight.getDestinationAirport());
            flightDTO.setDate(flight.getDate());
            return flightDTO;

        }catch (Exception e) { throw new RuntimeException("something went wrong" + e.getStackTrace() + e);}

    }

    public List<ResFlightDTO> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        if(flights.isEmpty()){
            return null;
        }
        List<ResFlightDTO> flightList = new ArrayList<>();
       try{
           for (Flight flight : flights){
               ResFlightDTO flightDTO = new ResFlightDTO();
               flightDTO.setId(flight.getFlight_id());
               flightDTO.setFlightNumber(flight.getFlightNumber());
               flightDTO.setDate(flight.getDate());
               flightDTO.setAirlineName(flight.getAirlineName());
               flightDTO.setAirportName(flight.getAirportName());
               flightDTO.setDepartureTime(flight.getDepartureTime());
               flightDTO.setDestinationAirport(flight.getDestinationAirport());
               flightDTO.setDate(flight.getDate());
               flightList.add(flightDTO);
           }
       }catch(Exception e){
           throw new RuntimeException("something went wrong"+e.getStackTrace()+e);
       }
        return flightList;
    }

    public ResFlightDTO getFlightById(Long id) {
        Optional<Flight> optionalFlight = flightRepository.findById(id);
        if(optionalFlight.isEmpty()) return  null;
        Flight flight = optionalFlight.get();
       try {
           ResFlightDTO flightDTO = new ResFlightDTO();
           flightDTO.setId(flight.getFlight_id());
           flightDTO.setFlightNumber(flight.getFlightNumber());
           flightDTO.setDate(flight.getDate());
           flightDTO.setAirlineName(flight.getAirlineName());
           flightDTO.setAirportName(flight.getAirportName());
           flightDTO.setDepartureTime(flight.getDepartureTime());
           flightDTO.setDestinationAirport(flight.getDestinationAirport());
           flightDTO.setDate(flight.getDate());
           return flightDTO;
       }catch(Exception e) { throw new RuntimeException("something went wrong" + e.getStackTrace() + e);}
    }

    public ResFlightDTO updateFlight(Long id, ReqFlightDTO reqFlightDTO) {
        Optional<Flight> existingFlightOpt = flightRepository.findById(id);
        if (existingFlightOpt.isEmpty()) return null;
        try{

                Flight flight = existingFlightOpt.get();
                flight.setFlight_id(id);
                flight.setFlightNumber(reqFlightDTO.getFlightNumber());
                flight.setDestinationAirport(reqFlightDTO.getDestinationAirport());
                flight.setAirportName(reqFlightDTO.getAirportName());
                flight.setDepartureTime(reqFlightDTO.getDepartureTime());
                flightRepository.save(flight);

                ResFlightDTO flightDTO = new ResFlightDTO();
                flightDTO.setFlightNumber(flight.getFlightNumber());
                flightDTO.setId(flight.getFlight_id());
                flightDTO.setDate(flight.getDate());
                flightDTO.setAirlineName(flight.getAirlineName());
                flightDTO.setAirportName(flight.getAirportName());
                flightDTO.setDestinationAirport(flight.getDestinationAirport());
                flightDTO.setDate(flight.getDate());
                flightDTO.setDepartureTime(flight.getDepartureTime());
                return flightDTO;

        }
        catch(Exception e) { throw new RuntimeException("Not able to find ticket id : "+ id + e.getStackTrace() + e);}

    }

    public ResponseDTO deleteFlight(Long id) {
        try{
            Optional<Flight> flight = flightRepository.findById(id);
            if(flight.isPresent()){
                flightRepository.deleteById(id);
                return new ResponseDTO("Flight Deleted Successfully ...");
            }
        }catch(Exception e) {
            throw new RuntimeException("Something Went Wrong" + e.getStackTrace() + e);
        }return null;
    }
}

package com.aaditya.airline_reservation_system.DTO;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Component;



@Component
@Getter
@Setter
public class ReqTicketDTO {
    @NonNull
    private String username;
}

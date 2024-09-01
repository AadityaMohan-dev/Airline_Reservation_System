package com.aaditya.airline_reservation_system.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ResPassengerDTO {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private String role;

}

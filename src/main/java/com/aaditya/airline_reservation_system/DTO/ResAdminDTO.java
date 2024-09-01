package com.aaditya.airline_reservation_system.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ResAdminDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
}

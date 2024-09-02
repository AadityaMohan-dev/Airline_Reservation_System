package com.aaditya.airline_reservation_system.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ReqUserDTO {
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String Phone;
}

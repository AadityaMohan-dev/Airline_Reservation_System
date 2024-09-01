package com.aaditya.airline_reservation_system.Entity;

import com.aaditya.airline_reservation_system.Enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userEntity")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;


    @Column(nullable = false)
    private String password;


    private RoleEnum role;
}

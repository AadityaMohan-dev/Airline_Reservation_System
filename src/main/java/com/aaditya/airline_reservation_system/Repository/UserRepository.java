package com.aaditya.airline_reservation_system.Repository;

import com.aaditya.airline_reservation_system.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}

package com.aaditya.airline_reservation_system.Repository;

import com.aaditya.airline_reservation_system.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {
}

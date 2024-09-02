package com.aaditya.airline_reservation_system.servicesTest;

import com.aaditya.airline_reservation_system.DTO.ReqUserDTO;
import com.aaditya.airline_reservation_system.DTO.ResAdminDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Entity.Admin;
import com.aaditya.airline_reservation_system.Entity.User;
import com.aaditya.airline_reservation_system.Enums.RoleEnum;
import com.aaditya.airline_reservation_system.Repository.AdminRepository;
import com.aaditya.airline_reservation_system.Repository.UserRepository;
import com.aaditya.airline_reservation_system.Services.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewAdmin_Success() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("admin");
        reqUserDTO.setPassword("password");
        reqUserDTO.setRePassword("password");
        reqUserDTO.setEmail("admin@example.com");

        User user = new User();
        user.setUsername(reqUserDTO.getUsername());
        user.setPassword(reqUserDTO.getPassword());
        user.setRole(RoleEnum.ADMIN);

        Admin admin = new Admin();
        admin.setAdmin_id(1L);
        admin.setEmail(reqUserDTO.getEmail());
        admin.setUser(user);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        ResponseDTO responseDTO = adminService.addNewAdmin(reqUserDTO);
        assertNotNull(responseDTO);
        assertEquals("New Admin Added Successfully.", responseDTO.getMessage());
    }

    @Test
    void testAddNewAdmin_PasswordMismatch() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("admin");
        reqUserDTO.setPassword("password");
        reqUserDTO.setRePassword("differentpassword");
        reqUserDTO.setEmail("admin@example.com");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.addNewAdmin(reqUserDTO);
        });
        assertEquals("Passwords do not match", exception.getMessage());
    }

    @Test
    void testGetAllAdminDetails_Success() throws Exception {
        Admin admin = new Admin();
        admin.setAdmin_id(1L);
        admin.setEmail("admin@example.com");
        User user = new User();
        user.setUsername("admin");
        admin.setUser(user);

        List<Admin> adminList = new ArrayList<>();
        adminList.add(admin);

        when(adminRepository.findAll()).thenReturn(adminList);

        List<ResAdminDTO> adminDTOList = adminService.getAllAdminDetails();
        assertNotNull(adminDTOList);
        assertEquals(1, adminDTOList.size());
        assertEquals("admin", adminDTOList.get(0).getUsername());
    }

    @Test
    void testGetAdminById_Found() {
        Admin admin = new Admin();
        admin.setAdmin_id(1L);
        admin.setEmail("admin@example.com");
        User user = new User();
        user.setUsername("admin");
        admin.setUser(user);

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        ResAdminDTO adminDTO = adminService.getAdminById(1L);
        assertNotNull(adminDTO);
        assertEquals("admin", adminDTO.getUsername());
    }

    @Test
    void testGetAdminById_NotFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        ResAdminDTO adminDTO = adminService.getAdminById(1L);
        assertNull(adminDTO);
    }

    @Test
    void testDeleteAdminById_Success() {
        Admin admin = new Admin();
        admin.setAdmin_id(1L);
        User user = new User();
        user.setId(1L);
        admin.setUser(user);

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        ResponseDTO responseDTO = adminService.deleteAdminById(1L);
        assertNotNull(responseDTO);
        assertEquals("User Deleted Successfully", responseDTO.getMessage());

        verify(adminRepository, times(1)).deleteById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAdminById_NotFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseDTO responseDTO = adminService.deleteAdminById(1L);
        assertNotNull(responseDTO);
        assertEquals("User/Admin with id : 1 Not Found", responseDTO.getMessage());
    }

    @Test
    void testUpdateAdminDetails_Success() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("updatedAdmin");
        reqUserDTO.setPassword("password");
        reqUserDTO.setRePassword("password");
        reqUserDTO.setEmail("updated@example.com");

        Admin existingAdmin = new Admin();
        existingAdmin.setAdmin_id(1L);
        User existingUser = new User();
        existingUser.setId(1L);
        existingAdmin.setUser(existingUser);
        existingAdmin.setEmail("old@example.com");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(existingAdmin));

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername(reqUserDTO.getUsername());
        updatedUser.setPassword(reqUserDTO.getPassword());
        updatedUser.setRole(RoleEnum.ADMIN);

        Admin updatedAdmin = new Admin();
        updatedAdmin.setAdmin_id(1L);
        updatedAdmin.setUser(updatedUser);
        updatedAdmin.setEmail(reqUserDTO.getEmail());

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(adminRepository.save(any(Admin.class))).thenReturn(updatedAdmin);

        ResAdminDTO adminDTO = adminService.updateAdminDetails(1L, reqUserDTO);
        assertNotNull(adminDTO);
        assertEquals("updatedAdmin", adminDTO.getUsername());
        assertEquals("updated@example.com", adminDTO.getEmail());
    }

    @Test
    void testUpdateAdminDetails_AdminNotFound() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("updatedAdmin");
        reqUserDTO.setPassword("password");
        reqUserDTO.setRePassword("password");
        reqUserDTO.setEmail("updated@example.com");

        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        ResAdminDTO adminDTO = adminService.updateAdminDetails(1L, reqUserDTO);
        assertNull(adminDTO);
    }
}

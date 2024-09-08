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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    private ReqUserDTO reqUserDTO;
    private Admin admin;
    private User user;
    private ResAdminDTO resAdminDTO;

    @BeforeEach
    public void setUp() {
        reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("adminUser");
        reqUserDTO.setPassword("adminPass");
        reqUserDTO.setEmail("admin@example.com");

        user = new User();
        user.setId(1L);
        user.setUsername("adminUser");
        user.setPassword("adminPass");
        user.setRole(RoleEnum.ADMIN);

        admin = new Admin();
        admin.setAdmin_id(1L);
        admin.setEmail("admin@example.com");
        admin.setUser(user);

        resAdminDTO = new ResAdminDTO();
        resAdminDTO.setId(1L);
        resAdminDTO.setUsername("adminUser");
        resAdminDTO.setRole("ADMIN");
        resAdminDTO.setEmail("admin@example.com");
    }

    @Test
    public void testAddNewAdmin_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        ResponseDTO responseDTO = adminService.addNewAdmin(reqUserDTO);

        assertNotNull(responseDTO);
        assertEquals("New Admin Added Successfully.", responseDTO.getMessage());
    }

    @Test
    public void testGetAllAdminDetails_Success() throws Exception {
        List<Admin> adminList = new ArrayList<>();
        adminList.add(admin);

        when(adminRepository.findAll()).thenReturn(adminList);

        List<ResAdminDTO> result = adminService.getAllAdminDetails();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(resAdminDTO.getId(), result.get(0).getId());
    }

    @Test
    public void testGetAdminById_Success() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        ResAdminDTO result = adminService.getAdminById(1L);

        assertNotNull(result);
        assertEquals(resAdminDTO.getId(), result.getId());
        assertEquals(resAdminDTO.getUsername(), result.getUsername());
    }

    @Test
    public void testGetAdminById_NotFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        ResAdminDTO result = adminService.getAdminById(1L);

        assertNull(result);
    }

    @Test
    public void testDeleteAdminById_Success() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        ResponseDTO responseDTO = adminService.deleteAdminById(1L);

        verify(adminRepository).deleteById(1L);
        verify(userRepository).deleteById(1L);
        assertNotNull(responseDTO);
        assertEquals("User Deleted Successfully", responseDTO.getMessage());
    }

    @Test
    public void testDeleteAdminById_NotFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseDTO responseDTO = adminService.deleteAdminById(1L);

        assertNotNull(responseDTO);
        assertEquals("User/Admin with id : 1 Not Found", responseDTO.getMessage());
    }

    @Test
    public void testUpdateAdminDetails_Success() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        ResAdminDTO result = adminService.updateAdminDetails(1L, reqUserDTO);

        assertNotNull(result);
        assertEquals(resAdminDTO.getId(), result.getId());
        assertEquals(reqUserDTO.getUsername(), result.getUsername());
    }

    @Test
    public void testUpdateAdminDetails_NotFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        ResAdminDTO result = adminService.updateAdminDetails(1L, reqUserDTO);

        assertNull(result);
    }
}

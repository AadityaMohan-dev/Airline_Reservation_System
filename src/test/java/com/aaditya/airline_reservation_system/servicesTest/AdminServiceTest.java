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
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewAdmin_Success() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("admin_user");
        reqUserDTO.setEmail("admin@example.com");

        User user = new User();
        user.setId(1L);
        user.setUsername("admin_user");
        user.setRole(RoleEnum.ADMIN);

        Admin admin = new Admin();
        admin.setAdmin_id(1L);
        admin.setEmail("admin@example.com");
        admin.setUser(user);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        ResponseDTO responseDTO = adminService.addNewAdmin(reqUserDTO);

        assertNotNull(responseDTO);
        assertEquals("New Admin Added Successfully.", responseDTO.getMessage());
    }

    @Test
    public void testGetAllAdminDetails_Success() throws Exception {
        Admin admin1 = new Admin();
        admin1.setAdmin_id(1L);
        admin1.setEmail("admin1@example.com");
        User user1 = new User();
        user1.setUsername("admin_user1");
        admin1.setUser(user1);

        Admin admin2 = new Admin();
        admin2.setAdmin_id(2L);
        admin2.setEmail("admin2@example.com");
        User user2 = new User();
        user2.setUsername("admin_user2");
        admin2.setUser(user2);

        List<Admin> admins = List.of(admin1, admin2);

        when(adminRepository.findAll()).thenReturn(admins);

        List<ResAdminDTO> responseList = adminService.getAllAdminDetails();

        assertNotNull(responseList);
        assertEquals(2, responseList.size());
        assertEquals("admin_user1", responseList.get(0).getUsername());
        assertEquals("admin_user2", responseList.get(1).getUsername());
    }

    @Test
    public void testGetAllAdminDetails_Empty() throws Exception {
        when(adminRepository.findAll()).thenReturn(new ArrayList<>());

        List<ResAdminDTO> responseList = adminService.getAllAdminDetails();

        assertNotNull(responseList);
        assertTrue(responseList.isEmpty());
    }

    @Test
    public void testGetAdminById_Found() {
        Admin admin = new Admin();
        admin.setAdmin_id(1L);
        admin.setEmail("admin@example.com");
        User user = new User();
        user.setUsername("admin_user");
        admin.setUser(user);

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        ResAdminDTO responseDTO = adminService.getAdminById(1L);

        assertNotNull(responseDTO);
        assertEquals("admin_user", responseDTO.getUsername());
        assertEquals("admin@example.com", responseDTO.getEmail());
    }

    @Test
    public void testGetAdminById_NotFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        ResAdminDTO responseDTO = adminService.getAdminById(1L);

        assertNull(responseDTO);
    }

    @Test
    public void testDeleteAdminById_Success() {
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
    public void testDeleteAdminById_NotFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseDTO responseDTO = adminService.deleteAdminById(1L);

        assertNotNull(responseDTO);
        assertEquals("User/Admin with id : 1 Not Found", responseDTO.getMessage());
        verify(adminRepository, never()).deleteById(anyLong());
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testUpdateAdminDetails_Success() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("updated_admin_user");
        reqUserDTO.setEmail("updated_admin@example.com");

        Admin existingAdmin = new Admin();
        existingAdmin.setAdmin_id(1L);
        existingAdmin.setEmail("admin@example.com");
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("admin_user");
        existingUser.setRole(RoleEnum.ADMIN);
        existingAdmin.setUser(existingUser);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updated_admin_user");
        updatedUser.setRole(RoleEnum.ADMIN);

        Admin updatedAdmin = new Admin();
        updatedAdmin.setAdmin_id(1L);
        updatedAdmin.setEmail("updated_admin@example.com");
        updatedAdmin.setUser(updatedUser);

        when(adminRepository.findById(1L)).thenReturn(Optional.of(existingAdmin));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(adminRepository.save(any(Admin.class))).thenReturn(updatedAdmin);

        ResAdminDTO responseDTO = adminService.updateAdminDetails(1L, reqUserDTO);

        assertNotNull(responseDTO);
        assertEquals("updated_admin_user", responseDTO.getUsername());
        assertEquals("updated_admin@example.com", responseDTO.getEmail());
    }

    @Test
    public void testUpdateAdminDetails_NotFound() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("updated_admin_user");
        reqUserDTO.setEmail("updated_admin@example.com");

        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        ResAdminDTO responseDTO = adminService.updateAdminDetails(1L, reqUserDTO);

        assertNull(responseDTO);
        verify(userRepository, never()).save(any(User.class));
        verify(adminRepository, never()).save(any(Admin.class));
    }
}

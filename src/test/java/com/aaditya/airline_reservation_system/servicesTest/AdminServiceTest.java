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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    public void testAddNewAdminSuccess() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("adminUser");
        reqUserDTO.setPassword("adminPass");
        reqUserDTO.setRePassword("adminPass");
        reqUserDTO.setEmail("admin@example.com");

        User user = new User();
        user.setId(1L);
        user.setUsername("adminUser");
        user.setPassword("adminPass");
        user.setRole(RoleEnum.ADMIN);

        Admin admin = new Admin();
        admin.setAdmin_id(1L);
        admin.setEmail("admin@example.com");
        admin.setUser(user);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        ResponseDTO responseDTO = adminService.addNewAdmin(reqUserDTO);

        assertEquals("New Admin Added Successfully.", responseDTO.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
        verify(adminRepository, times(1)).save(any(Admin.class));
    }

    @Test
    public void testAddNewAdminPasswordMismatch() {
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("adminUser");
        reqUserDTO.setPassword("adminPass");
        reqUserDTO.setRePassword("differentPass");
        reqUserDTO.setEmail("admin@example.com");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.addNewAdmin(reqUserDTO),
                "Expected addNewAdmin() to throw, but it didn't"
        );
        assertEquals("Passwords do not match", thrown.getMessage());
    }

    @Test
    public void testGetAdminByIdSuccess() {
        Long id = 1L;
        User user = new User();
        user.setUsername("adminUser");
        Admin admin = new Admin();
        admin.setAdmin_id(id);
        admin.setEmail("admin@example.com");
        admin.setUser(user);

        when(adminRepository.findById(id)).thenReturn(Optional.of(admin));

        ResAdminDTO adminDTO = adminService.getAdminById(id);

        assertNotNull(adminDTO);
        assertEquals("adminUser", adminDTO.getUsername());
        assertEquals(id, adminDTO.getId());
        assertEquals("ADMIN", adminDTO.getRole());
        assertEquals("admin@example.com", adminDTO.getEmail());
    }

    @Test
    public void testGetAdminByIdNotFound() {
        Long id = 1L;

        when(adminRepository.findById(id)).thenReturn(Optional.empty());

        ResAdminDTO adminDTO = adminService.getAdminById(id);

        assertNull(adminDTO);
    }

    @Test
    public void testDeleteAdminByIdSuccess() {
        Long id = 1L;
        Admin admin = new Admin();
        admin.setAdmin_id(id);

        when(adminRepository.findById(id)).thenReturn(Optional.of(admin));

        ResponseDTO responseDTO = adminService.deleteAdminById(id);

        assertEquals("User Deleted Successfully", responseDTO.getMessage());
        verify(adminRepository, times(1)).deleteById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteAdminByIdNotFound() {
        Long id = 1L;

        when(adminRepository.findById(id)).thenReturn(Optional.empty());

        ResponseDTO responseDTO = adminService.deleteAdminById(id);

        assertEquals("User/Admin with id : " + id + " Not Found", responseDTO.getMessage());
    }

    @Test
    public void testUpdateAdminDetailsSuccess() {
        Long id = 1L;
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("updatedUser");
        reqUserDTO.setPassword("newPass");
        reqUserDTO.setRePassword("newPass");
        reqUserDTO.setEmail("updated@example.com");

        User user = new User();
        user.setId(id);
        user.setUsername("updatedUser");
        user.setPassword("newPass");
        user.setRole(RoleEnum.ADMIN);

        Admin admin = new Admin();
        admin.setAdmin_id(id);
        admin.setEmail("updated@example.com");
        admin.setUser(user);

        when(adminRepository.findById(id)).thenReturn(Optional.of(admin));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        ResAdminDTO adminDTO = adminService.updateAdminDetails(id, reqUserDTO);

        assertNotNull(adminDTO);
        assertEquals("updated@example.com", adminDTO.getEmail());
        assertEquals(id, adminDTO.getId());
        assertEquals("ADMIN", adminDTO.getRole());
        assertEquals("updatedUser", adminDTO.getUsername());
    }

    @Test
    public void testUpdateAdminDetailsPasswordMismatch() {
        Long id = 1L;
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("updatedUser");
        reqUserDTO.setPassword("newPass");
        reqUserDTO.setRePassword("differentPass");
        reqUserDTO.setEmail("updated@example.com");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.updateAdminDetails(id, reqUserDTO),
                "Expected updateAdminDetails() to throw, but it didn't"
        );
        assertEquals("Passwords do not match", thrown.getMessage());
    }

    @Test
    public void testUpdateAdminDetailsNotFound() {
        Long id = 1L;
        ReqUserDTO reqUserDTO = new ReqUserDTO();
        reqUserDTO.setUsername("updatedUser");
        reqUserDTO.setPassword("newPass");
        reqUserDTO.setRePassword("newPass");
        reqUserDTO.setEmail("updated@example.com");

        when(adminRepository.findById(id)).thenReturn(Optional.empty());

        ResAdminDTO adminDTO = adminService.updateAdminDetails(id, reqUserDTO);

        assertNull(adminDTO);
    }
}

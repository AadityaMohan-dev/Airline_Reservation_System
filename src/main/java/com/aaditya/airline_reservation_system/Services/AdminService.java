package com.aaditya.airline_reservation_system.Services;

import com.aaditya.airline_reservation_system.DTO.ReqUserDTO;
import com.aaditya.airline_reservation_system.DTO.ResAdminDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Entity.Admin;
import com.aaditya.airline_reservation_system.Entity.User;
import com.aaditya.airline_reservation_system.Enums.RoleEnum;
import com.aaditya.airline_reservation_system.Repository.AdminRepository;
import com.aaditya.airline_reservation_system.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    UserRepository userRepository;
    public ResponseDTO addNewAdmin(ReqUserDTO reqUserDTO) {
        try{
            User user = new User();
            user.setUsername(reqUserDTO.getUsername());
            if(reqUserDTO.getPassword().equals(reqUserDTO.getRePassword())) {
                user.setPassword(reqUserDTO.getPassword());
            }else {
                throw new IllegalArgumentException("Passwords do not match");
            }
            user.setRole(RoleEnum.ADMIN);
            userRepository.save(user);
            Admin admin = new Admin();
            admin.setAdmin_id(user.getId());
            admin.setEmail(reqUserDTO.getEmail());
            admin.setUser(user);
            adminRepository.save(admin);
            ResponseDTO response = new ResponseDTO();
            response.setMessage("New Admin Added Successfully.");
            return response;
        }catch (Exception e){
            throw new RuntimeException("Something Went Wrong" + e);
        }
    }

    public List<ResAdminDTO> getAllAdminDetails() throws Exception {
        List<Admin> adminList = adminRepository.findAll();
        List<ResAdminDTO> adminDTOList = new ArrayList<>();
        try{
            for(Admin admin : adminList){
                ResAdminDTO admins = new ResAdminDTO();
                admins.setUsername(admin.getUser().getUsername());
                admins.setId(admin.getAdmin_id());
                admins.setRole("ADMIN");
                admins.setEmail(admin.getEmail());
                adminDTOList.add(admins);
            }
            return adminDTOList;
        }catch(Exception e){
            throw new RuntimeException("Failed to retrieve admin details: " + e.getMessage(), e);
        }
    }


    public ResAdminDTO getAdminById(Long id) {
        Optional<Admin> optional = adminRepository.findById(id);
        try{
            if(optional.isPresent()){
                Admin admin = optional.get();
                ResAdminDTO adminDTO = new ResAdminDTO();
                adminDTO.setEmail(admin.getEmail());
                adminDTO.setId(admin.getAdmin_id());
                adminDTO.setRole("ADMIN");
                adminDTO.setUsername(admin.getUser().getUsername());
                return adminDTO;
            }
        }catch(Exception e){
            throw new RuntimeException("User not Found with id :" + id + e.getStackTrace() + e);
        }
        return null;
    }

    public ResponseDTO deleteAdminById(Long id) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        ResponseDTO response = new ResponseDTO();
        if(optionalAdmin.isPresent()){
            adminRepository.deleteById(optionalAdmin.get().getUser().getId());
            userRepository.deleteById(id);
            response.setMessage("User Deleted Successfully");
            return response;
        }
        response.setMessage("User/Admin with id : "+id + " Not Found");
        return response;
    }

  public ResAdminDTO updateAdminDetails(Long id, ReqUserDTO reqUserDTO) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        if(optionalAdmin.isEmpty()){
            return null;
        }
        Admin admin = optionalAdmin.get();
        User user = new User();
        user.setId(admin.getUser().getId());
        user.setUsername(reqUserDTO.getUsername());
        user.setRole(RoleEnum.ADMIN);
        if(reqUserDTO.getPassword().equals(reqUserDTO.getRePassword())) {
            user.setPassword(reqUserDTO.getPassword());
        }else {
            throw new IllegalArgumentException("Passwords do not match");
        }

        Admin updatedAdmin = new Admin();
        updatedAdmin.setAdmin_id(id);
        updatedAdmin.setUser(user);
        updatedAdmin.setEmail(reqUserDTO.getEmail());
        try {
            userRepository.save(user);
            adminRepository.save(updatedAdmin);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while saving user or admin: " + e.getMessage(), e);
        }

        ResAdminDTO adminDTO = new ResAdminDTO();
        adminDTO.setEmail(updatedAdmin.getEmail());
        adminDTO.setId(updatedAdmin.getAdmin_id());
        adminDTO.setRole("ADMIN");
        adminDTO.setUsername(user.getUsername());

        return adminDTO;
    }

}
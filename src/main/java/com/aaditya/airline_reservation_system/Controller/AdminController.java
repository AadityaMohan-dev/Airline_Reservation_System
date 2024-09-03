package com.aaditya.airline_reservation_system.Controller;

import com.aaditya.airline_reservation_system.DTO.ReqUserDTO;
import com.aaditya.airline_reservation_system.DTO.ResAdminDTO;
import com.aaditya.airline_reservation_system.DTO.ResponseDTO;
import com.aaditya.airline_reservation_system.Services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping
    ResponseEntity<Object> addNewAdmin(@RequestBody ReqUserDTO reqUserDTO){
        ResponseDTO response = adminService.addNewAdmin(reqUserDTO);
        if(response != null){
            return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(response);
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(new ResponseDTO("Something Went Wrong"));
    }
    @GetMapping
    ResponseEntity<Object> getAllAdminDetails() throws Exception {
        List<ResAdminDTO> adminDTO =adminService.getAllAdminDetails();
        if(adminDTO.isEmpty()){
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new ResponseDTO("Something Went Wrong"));
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(adminDTO);
    }

    @GetMapping("/{id}")
    ResponseEntity<Object> getAdminDetails(@PathVariable Long id){
        ResAdminDTO admin =adminService.getAdminById(id);
        if(admin == null){
           return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new ResponseDTO("Admin Not Found with id : " + id));
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(admin);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteById(@PathVariable Long id){
        ResponseDTO response = adminService.deleteAdminById(id);
        if(response != null){
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(response);
        }
        return  ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new ResponseDTO("Admin Not Found with id : " + id));
    }

    @PutMapping("/{id}")
    ResponseEntity<Object> updateAdminProfile(@RequestBody ReqUserDTO reqUserDTO, @PathVariable Long id){
        ResAdminDTO adminDetails = adminService.updateAdminDetails(id,reqUserDTO);
        if(adminDetails != null){
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(adminDetails);
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new ResponseDTO("Admin Not Found with id : " + id));
    }
}

package com.deephire.Controllers;

import com.deephire.Dto.AdminCompanyDto;
import com.deephire.Dto.UserAdminDto;
import com.deephire.Enums.CompanyStatus;
import com.deephire.Enums.ERole;
import com.deephire.Models.Company;
import com.deephire.Models.User;
import com.deephire.Repositories.UserRepository;
import com.deephire.Service.AdminService;
import com.deephire.Service.CompanyService;
import com.deephire.Service.EmailService;
import com.deephire.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;
    @Autowired
    AdminService adminService;

    @Autowired
    CompanyService companyService;
    @Autowired
    EmailService emailService;
//
//    @GetMapping
//    public List<User>
//
//
//    getAllUsers1() {
//        return userService.findAll();
//    }

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        try{
            List<UserAdminDto> users = userService.findAllUsers();
            return ResponseEntity.ok(users);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PutMapping("/{userId}/ban")
    public ResponseEntity<?> banUser(@PathVariable Long userId) {
        try{
        adminService.banUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PutMapping("/{userId}/unban")
    public ResponseEntity<?> unbanUser(@PathVariable Long userId) {
        try{
        adminService.unbanUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(true);}
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }



    @PutMapping("/company/{companyId}/reject")
    public ResponseEntity<?> rejectCompany(@PathVariable Long companyId) {
        try {

           return ResponseEntity.status(HttpStatus.OK).body(companyService.rejectCompanyProfile(companyId));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }


    @PutMapping("/company/{companyId}/approve")
    public ResponseEntity<?> approveCompany(@PathVariable Long companyId) {
        try {

            return ResponseEntity.status(HttpStatus.OK).body(companyService.approveCompanyProfile(companyId));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }


    @GetMapping("/all-companies")
    public ResponseEntity<List<AdminCompanyDto>> all() {
        try {
            List<Company> companies = companyService.findAll();

            if (companies.isEmpty()) {
                return new ResponseEntity<>( HttpStatus.NO_CONTENT);
            }


            List<AdminCompanyDto> adminCompanyDtos =   companies.stream()
                    .map(company -> {
                        AdminCompanyDto adminCompanyDto = new AdminCompanyDto();
                        adminCompanyDto.setIdCompany(company.getId());
                        adminCompanyDto.setFirstName(company.getAdmin().getFirstName());
                        adminCompanyDto.setEmail(company.getAdmin().getEmail());
                        adminCompanyDto.setFirstName(company.getAdmin().getFirstName());
                        adminCompanyDto.setLastName(company.getAdmin().getLastName());
                        adminCompanyDto.setFile(Base64.getEncoder().encodeToString
                                (company.getAdmin().getFile()));
                        adminCompanyDto.setIndustry(company.getIndustry());
                        adminCompanyDto.setLocation(company.getLocation());
                        adminCompanyDto.setLogo(Base64.getEncoder().encodeToString
                                (company.getLogo()));
                        adminCompanyDto.setStatus(company.getStatus() == null? CompanyStatus.PENDING.name():
                                company.getStatus().name());


                        return adminCompanyDto;
                    })
                    .toList();
            return new ResponseEntity<>(adminCompanyDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}

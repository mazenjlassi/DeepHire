package com.deephire.Controllers;

import com.deephire.Dto.KpiDto;
import com.deephire.Enums.ERole;
import com.deephire.JWT.JwtUtils;
import com.deephire.Models.AdminCompany;
import com.deephire.Models.Company;
import com.deephire.Models.RHCompany;
import com.deephire.Models.User;
import com.deephire.Repositories.CompanyRepository;
import com.deephire.Repositories.JobPostingRepository;
import com.deephire.Repositories.StatusOfCvRepository;
import com.deephire.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private StatusOfCvRepository statusOfCvRepository;



    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/kpi")
    public ResponseEntity<?> getKpis(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return new ResponseEntity<>("Token is missing or invalid", HttpStatus.BAD_REQUEST);
            }

            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));


            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(role -> role.getName() == ERole.ROLE_ADMIN);

            if (!isAdmin) {
                return new ResponseEntity<>("You are not allowed to access this resource", HttpStatus.UNAUTHORIZED);
            }



            long usersCount =userRepository.count();
            long jobPostingCount = jobPostingRepository.count();
            long cvsCount = statusOfCvRepository.count();
            long companiesCount = companyRepository.count();

            List<KpiDto> kpis = new ArrayList<>();
            kpis.add(new KpiDto("Users", usersCount));
            kpis.add(new KpiDto("Job Postings", jobPostingCount));
            kpis.add(new KpiDto("CVs Received", cvsCount));
            kpis.add(new KpiDto("Companies", companiesCount));
            return  new ResponseEntity<>(kpis, HttpStatus.OK) ;
        }
        catch (Exception e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }




    //****************************** uses-pre-month

    @GetMapping("/users-per-month")
    public ResponseEntity<?> getRecruitersPerMonth(@RequestHeader("Authorization") String token) {


        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return new ResponseEntity<>("Token is missing or invalid", HttpStatus.BAD_REQUEST);
            }

            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));


            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(role -> role.getName() == ERole.ROLE_ADMIN);

            if (!isAdmin) {
                return new ResponseEntity<>("You are not allowed to access this resource", HttpStatus.UNAUTHORIZED);
            }







            List<Object[]>monthlyCounts = userRepository.getUsersPerMonth();

            return  new ResponseEntity<>(monthlyCounts, HttpStatus.OK) ;
        }
        catch (Exception e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }



}

package com.deephire.Controllers;


import com.deephire.Dto.KpiDto;
import com.deephire.Dto.UserCompanyDto;
import com.deephire.JWT.JwtUtils;
import com.deephire.Models.AdminCompany;
import com.deephire.Models.Company;
import com.deephire.Models.RHCompany;
import com.deephire.Models.User;
import com.deephire.Repositories.*;
import com.deephire.Service.JobPostingService;
import com.deephire.Service.RHCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/admin-company/dashboard")
public class AdminCompanyDashboardController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private StatusOfCvRepository statusOfCvRepository;


    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private RHCompanyService rhCompanyService;


    @Autowired
    private RHCompanyRepository rhCompanyRepository;


    @Autowired
    private JobPostingService jobPostingService;

    @GetMapping("/kpi")
    public ResponseEntity<?> getKpis(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return new ResponseEntity<>("Token is missing or invalid", HttpStatus.BAD_REQUEST);
            }

            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Company company;

            if (user instanceof AdminCompany adminCompany) {
                company = companyRepository.getCompanyByAdmin(adminCompany);
            } else if (user instanceof RHCompany rhCompany) {
                company = rhCompany.getCompany(); // relation ManyToOne
            } else {
                return new ResponseEntity<>("User must be AdminCompany or RHCompany", HttpStatus.UNAUTHORIZED);
            }

            if (company == null) {
                return new ResponseEntity<>("Associated company not found", HttpStatus.NOT_FOUND);
            }

            long recruitersCount =rhCompanyRepository.countByCompany(company);
            long jobsCount = jobPostingRepository.countByCompany(company);
            long cvsCount = statusOfCvRepository.countByJobPosting_Company(company);

        List<KpiDto> kpis = new ArrayList<>();
        kpis.add(new KpiDto("RH Recruiters", recruitersCount));
        kpis.add(new KpiDto("Job Postings", jobsCount));
        kpis.add(new KpiDto("CVs Received", cvsCount));
        return  new ResponseEntity<>(kpis, HttpStatus.OK) ;
    }
        catch (Exception e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }



    @GetMapping("/recruiters-per-month")
    public ResponseEntity<?> getRecruitersPerMonth(@RequestHeader("Authorization") String token) {


        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return new ResponseEntity<>("Token is missing or invalid", HttpStatus.BAD_REQUEST);
            }

            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Company company;

            if (user instanceof AdminCompany adminCompany) {
                company = companyRepository.getCompanyByAdmin(adminCompany);
            } else if (user instanceof RHCompany rhCompany) {
                company = rhCompany.getCompany(); // relation ManyToOne
            } else {
                return new ResponseEntity<>("User must be AdminCompany or RHCompany", HttpStatus.UNAUTHORIZED);
            }

            if (company == null) {
                return new ResponseEntity<>("Associated company not found", HttpStatus.NOT_FOUND);
            }



            List<Integer> monthlyCounts = rhCompanyService.getRecruitersCountPerMonth(company);
            return  new ResponseEntity<>(monthlyCounts, HttpStatus.OK) ;
        }
        catch (Exception e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        }




    @GetMapping("/job-postings-per-month")
    public ResponseEntity<?> getJobPostingsPerMonth(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return new ResponseEntity<>("Token is missing or invalid", HttpStatus.BAD_REQUEST);
            }

            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Company company;
            if (user instanceof AdminCompany adminCompany) {
                company = companyRepository.getCompanyByAdmin(adminCompany);
            } else if (user instanceof RHCompany rhCompany) {
                company = rhCompany.getCompany();
            } else {
                return new ResponseEntity<>("User must be AdminCompany or RHCompany", HttpStatus.UNAUTHORIZED);
            }

            if (company == null) {
                return new ResponseEntity<>("Associated company not found", HttpStatus.NOT_FOUND);
            }

            // Service returns list of 12 elements (one for each month)
            List<Integer> monthlyCounts = jobPostingService.getJobPostingsPerMonth(company);

            return ResponseEntity.ok(monthlyCounts);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/information-welcome-card")
    public ResponseEntity<?> getInformationWelcomeCard(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return new ResponseEntity<>("Token is missing or invalid", HttpStatus.BAD_REQUEST);
            }

            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Company company;
            if (user instanceof AdminCompany adminCompany) {
                company = companyRepository.getCompanyByAdmin(adminCompany);
            } else if (user instanceof RHCompany rhCompany) {
                company = rhCompany.getCompany();
            } else {
                return new ResponseEntity<>("User must be AdminCompany or RHCompany", HttpStatus.UNAUTHORIZED);
            }

            if (company == null) {
                return new ResponseEntity<>("Associated company not found", HttpStatus.NOT_FOUND);
            }

            UserCompanyDto userCompanyDto = new UserCompanyDto();
            userCompanyDto.setCompanyName(company.getName());
            userCompanyDto.setFirstName(user.getFirstName());
            userCompanyDto.setLastName(user.getLastName());
            userCompanyDto.setEmail(user.getEmail());
            userCompanyDto.setLogo(Base64.getEncoder().encodeToString(company.getLogo()));
            userCompanyDto.setLocation(company.getLocation());
            userCompanyDto.setDescription(company.getDescription());
            userCompanyDto.setIndustry(company.getIndustry());





            return ResponseEntity.ok(userCompanyDto);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}

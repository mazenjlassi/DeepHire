package com.deephire.Controllers;

import com.deephire.Dto.ApplicationStatusCountDTO;
import com.deephire.Dto.KpiDto;
import com.deephire.Enums.ERole;
import com.deephire.JWT.JwtUtils;
import com.deephire.Models.*;
import com.deephire.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private AdminCompanyRepository adminCompanyRepository;



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







            List<Object[]>counts = userRepository.getUsersPerMonth();
            List<Integer> monthlyCounts = counts.stream()
                    .map(objArr -> ((Number) objArr[1]).intValue())
                    .collect(Collectors.toList());


            return  new ResponseEntity<>(monthlyCounts, HttpStatus.OK) ;
        }
        catch (Exception e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }

    //****************************** Company-pre-month

    @GetMapping("/company-per-month")
    public ResponseEntity<?> getCompanyPerMonth(@RequestHeader("Authorization") String token) {


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







            List<Object[]>counts = companyRepository.getCompaniesPerMonth();
            List<Integer> monthlyCounts = counts.stream()
                    .map(objArr -> ((Number) objArr[1]).intValue())
                    .collect(Collectors.toList());
            return  new ResponseEntity<>(monthlyCounts, HttpStatus.OK) ;
        }
        catch (Exception e){
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }


    @GetMapping("/findTop5CompaniesByJobPostingsCount")
    public ResponseEntity<List<Company>> top10JobPostings() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(companyRepository.findTop5CompaniesByJobPostingsCount()) ;
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @GetMapping("/application-status")
    public List<ApplicationStatusCountDTO> getApplicationStatusData() {
        return statusOfCvRepository.countApplicationsByStatus();
    }



    @GetMapping("/get-monthly-job-postings")
    public ResponseEntity<List<Integer> > jobPostingsMonthly() {

        try {

            List<Object[]>counts = jobPostingRepository.getMonthlyJobPostings();
            List<Integer> jobPostings = counts.stream()
                    .map(objArr -> ((Number) objArr[1]).intValue())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(jobPostings);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @GetMapping("/get-top-5-by-copany-created")
    public ResponseEntity<List<Company>> findTop5ByOrderByCompanyCreatedAtDesc() {

        try {

            Pageable topFive = PageRequest.of(0, 5);  // page 0, size 5
            List<Company> last5Admins = adminCompanyRepository.findTop5ByMostJobPostings();
            return  new ResponseEntity<>(last5Admins, HttpStatus.OK) ;
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }



}

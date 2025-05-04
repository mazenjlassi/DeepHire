package com.deephire.Controllers;


import com.deephire.Dto.CompanyDto;
import com.deephire.JWT.JwtUtils;
import com.deephire.Models.Company;
import com.deephire.Models.Profile;
import com.deephire.Models.User;
import com.deephire.Repositories.UserRepository;
import com.deephire.Request.ProfileCompletionRequest;
import com.deephire.Service.AdminCompanyService;
import com.deephire.Models.AdminCompany;
import com.deephire.Service.CommentService;
import com.deephire.Service.CompanyService;
import com.deephire.Service.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin-company")
public class AdminCompanyRestController {

    @Autowired
    private AdminCompanyService adminCompanyService;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CompanyService companyService;

    @PostMapping(value = "/complete-profile-company", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> completeProfile(
            @RequestHeader("Authorization") String token,
            @RequestPart("profileData") String companyDataJson,
            @RequestPart(value = "profilePicture", required = false) MultipartFile logoCompany,
            @RequestPart(value = "backGroundImage", required = false) MultipartFile backGroundImage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompanyDto companyRequest = objectMapper.readValue(companyDataJson, CompanyDto.class);

            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            AdminCompany user = adminCompanyService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create new company or use existing
            Company company = user.getCompany();
            if (company == null) {
                company = new Company();
                user.setCompany(company);
            }

            // Fill company details
            company.setName(companyRequest.getName());
            company.setIndustry(companyRequest.getIndustry());
            company.setLocation(companyRequest.getLocation());
            company.setDescription(companyRequest.getDescription());

            if (logoCompany != null && !logoCompany.isEmpty()) {
                company.setLogo(logoCompany.getBytes());
            }

            if (backGroundImage != null && !backGroundImage.isEmpty()) {
                company.setBackgroundImage(backGroundImage.getBytes());
            }

            // Set the bi-directional relationship
            company.setAdmin(user);     // Company → User
            user.setCompany(company);   // User → Company

            user.setFirstLogin(true);


            // Now save only the user
            adminCompanyService.update(user);
            // (company will be saved automatically if you have CascadeType.ALL)
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new MessageResponse("Company profile completed successfully"));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new MessageResponse("error  "));
        }

        }



    @PutMapping(value = "/update-profile-company", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestPart("profileData") String companyDataJson,
            @RequestPart(value = "profilePicture", required = false) MultipartFile logoCompany,
            @RequestPart(value = "backGroundImage", required = false) MultipartFile backGroundImage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompanyDto companyRequest = objectMapper.readValue(companyDataJson, CompanyDto.class);

            String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            AdminCompany user = adminCompanyService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create new company or use existing
            Company company = user.getCompany();
            company.setName(companyRequest.getName());
            company.setIndustry(companyRequest.getIndustry());
            company.setLocation(companyRequest.getLocation());
            company.setDescription(companyRequest.getDescription());
            if (logoCompany != null && !logoCompany.isEmpty()) {
                company.setLogo(logoCompany.getBytes());
            }
            if (backGroundImage != null && !backGroundImage.isEmpty()) {
                company.setBackgroundImage(backGroundImage.getBytes());
            }

            // Set the bi-directional relationship
            company.setAdmin(user);     // Company → User
            user.setCompany(company);   // User → Company
            // Now save only the user
            adminCompanyService.update(user);
            // (company will be saved automatically if you have CascadeType.ALL)
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new MessageResponse("update profile completed successfully"));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new MessageResponse("error  "));
        }

    }
    @PostMapping("/add")
    public ResponseEntity<AdminCompany> add(@RequestBody AdminCompany adminCompany) {
        try {
            adminCompanyService.add(adminCompany);
            return new ResponseEntity<>(adminCompany, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public  ResponseEntity<List<AdminCompany>> all(){
        try{
            List<AdminCompany> adminsCompanie=adminCompanyService.all();
            if(adminsCompanie.isEmpty()){
                return new ResponseEntity<List<AdminCompany>>(adminsCompanie,HttpStatus.NO_CONTENT);
            }

            return  new ResponseEntity<List<AdminCompany>>(adminsCompanie,HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<List<AdminCompany>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/findById/{id}")
    public  ResponseEntity<AdminCompany> findById(@PathVariable Long id){
        AdminCompany adminCompany = adminCompanyService.findById(id);
        if(adminCompany != null){
             return new  ResponseEntity<AdminCompany>(adminCompany,HttpStatus.OK);

        }
        else {
            return  new ResponseEntity<AdminCompany>(HttpStatus.NOT_FOUND);
        }
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<AdminCompany> update(@PathVariable Long id, @RequestBody AdminCompany adminCompany) {
        AdminCompany existingAdminCompany = adminCompanyService.findById(id);
        if (existingAdminCompany != null) {
            // Pas besoin de redéfinir l'ID, l'ID sera automatiquement utilisé lors de la mise à jour
            adminCompanyService.update(adminCompany);
            return new ResponseEntity<>(adminCompany, HttpStatus.OK);
        }
        throw new RuntimeException("Failed to update Admin company");
    }




    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<HttpStatus> delete(@PathVariable Long id){
        try {
             adminCompanyService.delete(id);
             return  new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.deephire.Controllers;

import com.deephire.Service.AdminCompanyService;
import com.deephire.Models.AdminCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin-company")
public class AdminCompanyRestController {

    @Autowired
    private AdminCompanyService adminCompanyService;

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

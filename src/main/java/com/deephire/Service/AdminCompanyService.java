package com.deephire.Service;

import com.deephire.Repositories.AdminCompanyRepository;
import com.deephire.models.AdminCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminCompanyService {

    @Autowired
    private AdminCompanyRepository adminCompanyRepository;

    public AdminCompany add(AdminCompany adminCompany){
        return adminCompanyRepository.save(adminCompany);
    }

    public AdminCompany findById (Long id){
         return adminCompanyRepository.findById(id).get();
    }

    public void delete (Long id){
         adminCompanyRepository.deleteById(id);
    }

    public AdminCompany update (AdminCompany adminCompany){
      return adminCompanyRepository.saveAndFlush(adminCompany);
    }

    public List<AdminCompany> all(){
        return adminCompanyRepository.findAll();
    }

}

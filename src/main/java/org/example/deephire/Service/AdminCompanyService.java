package org.example.deephire.Service;

import org.example.deephire.Repositories.AdminCompanyRepository;
import org.example.deephire.models.AdminCompany;
import org.example.deephire.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminCompanyService {

     @Autowired
     private AdminCompanyRepository adminCompanyRepository;

     public AdminCompany addAdmin (AdminCompany adminCompany){return adminCompanyRepository.save(adminCompany);}

}

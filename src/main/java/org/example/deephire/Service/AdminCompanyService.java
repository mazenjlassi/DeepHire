package org.example.deephire.Service;

import org.example.deephire.Repositories.AdminCompanyRepository;
import org.example.deephire.models.AdminCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminCompanyService {
    @Autowired
    private AdminCompanyRepository adminCompanyRepository;

    public AdminCompany add(AdminCompany adminCompany){
        return adminCompanyRepository.save(adminCompany);
    }
}

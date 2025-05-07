package com.deephire.Service;

import com.deephire.Enums.CompanyStatus;
import com.deephire.Models.AdminCompany;
import com.deephire.Models.User;
import com.deephire.Repositories.CompanyRepository;
import com.deephire.Models.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EmailService emailService;

    public Company add(Company company) { return companyRepository.save(company); }

    public Company find(Long id) { return companyRepository.findById(id).get(); }

    public void delete(Long id) { companyRepository.deleteById(id); }

    public Company update(Company company) { return companyRepository.saveAndFlush(company); }

    public List<Company> findAll() { return companyRepository.findAll(); }


    public  Company rejectCompanyProfile(Long companyId){
        Company company = companyRepository.findById(companyId).get();
        company.setStatus(CompanyStatus.REJECTED);
        emailService.sendCompanyStatusEmail(company.getAdmin().getEmail(),company.getAdmin().getUsername(),company.getName(),false);

        return companyRepository.saveAndFlush(company);
    }

    public Company approveCompanyProfile(Long companyId) {
        Company company = companyRepository.findById(companyId).get();
        company.setStatus(CompanyStatus.APPROVED);
        emailService.sendCompanyStatusEmail(company.getAdmin().getEmail(),company.getAdmin().getUsername(),company.getName(),true);
        return companyRepository.saveAndFlush(company);
    }

    public Company deleteCompanyProfile(Long companyId) {
        Company company = companyRepository.findById(companyId).get();
        company.setStatus(CompanyStatus.APPROVED);
        return companyRepository.saveAndFlush(company);
    }




}

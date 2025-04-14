package com.deephire.Service;

import com.deephire.Repositories.CompanyRepository;
import com.deephire.Models.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Company add(Company company) { return companyRepository.save(company); }

    public Company find(Long id) { return companyRepository.findById(id).get(); }

    public void delete(Long id) { companyRepository.deleteById(id); }

    public Company update(Company company) { return companyRepository.saveAndFlush(company); }

    public List<Company> findAll() { return companyRepository.findAll(); }
}

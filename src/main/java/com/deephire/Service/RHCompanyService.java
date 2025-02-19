package com.deephire.Service;

import com.deephire.Repositories.RHCompanyRepository;
import com.deephire.models.RHCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RHCompanyService {
    @Autowired
    private RHCompanyRepository rhCompanyRepository;

    public RHCompany add(RHCompany rhCompany) { return rhCompanyRepository.save(rhCompany); }

    public RHCompany find(Long id) { return rhCompanyRepository.findById(id).get(); }

    public void delete(Long id) { rhCompanyRepository.deleteById(id); }

    public RHCompany update(RHCompany rhCompany) { return rhCompanyRepository.saveAndFlush(rhCompany); }

    public List<RHCompany> findAll() { return rhCompanyRepository.findAll(); }
}

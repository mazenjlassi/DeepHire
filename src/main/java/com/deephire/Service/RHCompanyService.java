package com.deephire.Service;

import com.deephire.Models.Company;
import com.deephire.Models.RHCompany;
import com.deephire.Models.User;
import com.deephire.Repositories.RHCompanyRepository;
import com.deephire.Models.Role;
import com.deephire.Enums.ERole;
import com.deephire.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RHCompanyService {

    @Autowired
    private RHCompanyRepository rhCompanyRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    EmailService emailService;

    public RHCompany add(RHCompany rhCompany, Company company) {
        String rawPassword = rhCompany.getPassword();

        rhCompany.setPassword(passwordEncoder.encode(rawPassword));

        Role recruiterRole = roleRepository.findByName(ERole.ROLE_RECRUITER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        rhCompany.setRoles(Collections.singleton(recruiterRole));
        rhCompany.setCompany(company);
        RHCompany saved = rhCompanyRepository.save(rhCompany);

        emailService.sendWelcomeEmail(rhCompany.getEmail(), rhCompany.getUsername(), rawPassword);

        return saved;
    }


    public List<RHCompany> findAll() {
        return rhCompanyRepository.findAll();
    }

    public RHCompany find(Long id) {
        return rhCompanyRepository.findById(id).orElse(null);
    }

    public RHCompany update(RHCompany rhCompany) {
        if (rhCompany.getPassword() != null && !rhCompany.getPassword().isEmpty()) {
            rhCompany.setPassword(passwordEncoder.encode(rhCompany.getPassword()));
        }
        return rhCompanyRepository.save(rhCompany);
    }

    public void delete(Long id) {
        rhCompanyRepository.deleteById(id);
    }

    // New method to find all users with ROLE_RECRUITER
    public List<RHCompany> findAllRecruiters() {
        return rhCompanyRepository.findAll().stream()
                .filter(rhCompany -> rhCompany.getRoles().stream()
                        .anyMatch(role -> role.getName() == ERole.ROLE_RECRUITER))
                .collect(Collectors.toList());
    }


}
package com.deephire.Service;



import com.deephire.Models.AdminCompany;
import com.deephire.Repositories.AdminCompanyRepository;
import com.deephire.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

@Service
public class AdminCompanyService {

    @Autowired
    private AdminCompanyRepository adminCompanyRepository;

    @Autowired
    private UserRepository userRepository;


    public List<AdminCompany> all() {
        return adminCompanyRepository.findAll();
    }

    public AdminCompany add(AdminCompany adminCompany) {
        return adminCompanyRepository.save(adminCompany);
    }

    public AdminCompany update(AdminCompany adminCompany) {
        return adminCompanyRepository.saveAndFlush(adminCompany);
    }

    public void delete(Long id) {
        adminCompanyRepository.deleteById(id);
    }

    public AdminCompany findById(Long id) {
        return adminCompanyRepository.findById(id).orElse(null);
    }


    public Optional<AdminCompany> findByUsername(String username){
        return adminCompanyRepository.findByUsername(username);
    }


    @Transactional
    public void verify(Long id) {
        adminCompanyRepository.verifyAdminCompany(id);
    }

}

package com.deephire.Service;

import com.deephire.Enums.CompanyStatus;
import com.deephire.Enums.ERole;
import com.deephire.Models.Company;
import com.deephire.Models.User;
import com.deephire.Repositories.CompanyRepository;
import com.deephire.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyRepository companyRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void banUser(Long userId) {
        User user = getUserOrThrow(userId);
        user.setBanned(true);
        userRepository.save(user);
    }

    public void unbanUser(Long userId) {
        User user = getUserOrThrow(userId);
        user.setBanned(false);
        userRepository.save(user);
    }

    public void approveCompanyProfile(Long userId) {
        User user = getUserOrThrow(userId);
        if (hasRole(user, ERole.ROLE_ADMINCOMPANY)) {
            Company profile = getCompanyProfileOrThrow(userId);
            profile.setStatus(CompanyStatus.APPROVED);
            companyRepository.save(profile);
        } else {
            throw new IllegalStateException("User is not a company admin.");
        }
    }

    public void rejectCompanyProfile(Long userId) {
        User user = getUserOrThrow(userId);
        if (hasRole(user, ERole.ROLE_ADMINCOMPANY)) {
            Company profile = getCompanyProfileOrThrow(userId);
            profile.setStatus(CompanyStatus.REJECTED);
            companyRepository.save(profile);
        } else {
            throw new IllegalStateException("User is not a company admin.");
        }
    }

    // Helper methods
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));
    }

    private Company getCompanyProfileOrThrow(Long userId) {
        return companyRepository.findCompanyByAdmin_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Company profile not found for user id " + userId));
    }

    private boolean hasRole(User user, ERole role) {
        return user.getRoles().stream()
                .anyMatch(r -> r.getName().equals(role));
    }
}

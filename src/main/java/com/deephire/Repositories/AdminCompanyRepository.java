package com.deephire.Repositories;

import com.deephire.Models.AdminCompany;
import com.deephire.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface AdminCompanyRepository extends JpaRepository<AdminCompany,Long> {
    Optional<AdminCompany> findByUsername(String username);
}

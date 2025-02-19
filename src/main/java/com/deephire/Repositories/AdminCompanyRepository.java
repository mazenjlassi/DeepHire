package com.deephire.Repositories;

import com.deephire.models.AdminCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AdminCompanyRepository extends JpaRepository<AdminCompany,Long> {

}

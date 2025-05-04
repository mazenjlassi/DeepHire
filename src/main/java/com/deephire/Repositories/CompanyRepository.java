package com.deephire.Repositories;

import com.deephire.Models.AdminCompany;
import com.deephire.Models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {
    Company getCompanyByAdmin(AdminCompany user);


}

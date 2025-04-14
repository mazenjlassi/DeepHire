package com.deephire.Repositories;

import com.deephire.Models.RHCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RHCompanyRepository extends JpaRepository<RHCompany,Long> {
}

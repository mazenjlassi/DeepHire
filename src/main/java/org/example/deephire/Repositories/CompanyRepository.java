package org.example.deephire.Repositories;

import org.example.deephire.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Long, Company> {
}

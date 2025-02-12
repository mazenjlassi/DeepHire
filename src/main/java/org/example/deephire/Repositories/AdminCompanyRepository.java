package org.example.deephire.Repositories;

import org.example.deephire.models.AdminCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminCompanyRepository extends JpaRepository<AdminCompany,Long> {
}

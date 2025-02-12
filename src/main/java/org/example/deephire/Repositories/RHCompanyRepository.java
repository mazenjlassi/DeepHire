package org.example.deephire.Repositories;

import org.example.deephire.models.RHCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RHCompanyRepository extends JpaRepository<RHCompany,Long> {
}

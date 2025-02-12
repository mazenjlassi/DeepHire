package org.example.deephire.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RHCompany extends JpaRepository<org.example.deephire.models.RHCompany,Long> {
}

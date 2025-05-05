package com.deephire.Repositories;

import com.deephire.Models.AdminCompany;
import com.deephire.Models.Company;
import com.deephire.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository

public interface AdminCompanyRepository extends JpaRepository<AdminCompany,Long> {
    Optional<AdminCompany> findByUsername(String username);

    @Modifying
    @Query("UPDATE AdminCompany a SET a.isValid = true WHERE a.id = :id")
    void verifyAdminCompany(@Param("id") Long id);

    @Query(value = """
        SELECT c.* 
        FROM company c 
        LEFT JOIN job_posting j ON c.id = j.company_id 
        GROUP BY c.id 
        ORDER BY COUNT(j.id) DESC 
        LIMIT 5
        """, nativeQuery = true)
    List<Company> findTop5ByMostJobPostings();
}

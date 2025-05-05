package com.deephire.Repositories;

import com.deephire.Models.AdminCompany;
import com.deephire.Models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {
    Company getCompanyByAdmin(AdminCompany user);

    long count();

    @Query(value = """
        SELECT m.month_num AS month, 
               COALESCE(COUNT(c.id), 0) AS count
        FROM (
            SELECT 1 AS month_num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION
            SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION
            SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12
        ) AS m
        LEFT JOIN company c ON MONTH(c.created_at) = m.month_num 
        GROUP BY m.month_num
        ORDER BY m.month_num
    """, nativeQuery = true)
    List<Object[]> getCompaniesPerMonth();




    @Query(value = """
        SELECT c.* 
        FROM company c 
        LEFT JOIN job_posting j ON c.id = j.company_id 
        GROUP BY c.id 
        ORDER BY COUNT(j.id) DESC 
        LIMIT 5
        """, nativeQuery = true)
    List<Company> findTop5CompaniesByJobPostingsCount();

}

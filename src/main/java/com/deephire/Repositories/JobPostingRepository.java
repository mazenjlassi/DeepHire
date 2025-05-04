package com.deephire.Repositories;

import com.deephire.Models.Company;
import com.deephire.Models.JobPosting;
import com.deephire.Models.RHCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    List<JobPosting> findJobPostingByCompany(Company company);

    Optional<JobPosting> findByTitleAndDescriptionAndRequirementsAndLocationAndDatePosted(
            String title,
            String description,
            String requirements,
            String location,
            Date datePosted
    );

    void deleteByTitleAndDescriptionAndRequirementsAndLocationAndDatePosted(
            String title,
            String description,
            String requirements,
            String location,
            Date datePosted
    );

    long countByCompany(Company company);

    @Query(value = """
        SELECT m.month_num AS month, 
               COALESCE(COUNT(j.id), 0) AS count
        FROM (
            SELECT 1 AS month_num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION
            SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION
            SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12
        ) AS m
        LEFT JOIN job_posting j ON MONTH(j.date_posted) = m.month_num AND j.company_id = :companyId
        GROUP BY m.month_num
        ORDER BY m.month_num
    """, nativeQuery = true)
    List<Object[]> getMonthlyJobPostingsByCompanyNative(@Param("companyId") Long companyId);



}

package com.deephire.Repositories;

import com.deephire.Models.Company;
import com.deephire.Models.JobPosting;
import com.deephire.Models.RHCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting,Long > {
    List<JobPosting> findJobPostingByCompany(Company company);
}

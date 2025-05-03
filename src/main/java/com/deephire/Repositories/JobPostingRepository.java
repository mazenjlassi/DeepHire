package com.deephire.Repositories;

import com.deephire.Models.Company;
import com.deephire.Models.JobPosting;
import com.deephire.Models.RHCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting,Long > {
    List<JobPosting> findJobPostingByCompany(Company company);


    Optional<JobPosting> findByTitleAndDescriptionAndRequirementsAndLocationAndDatePosted(
            String title,
            String description,
            String requirements,
            String location,
            Date datePosted
    );

    // Custom delete method based on those fields
    void deleteByTitleAndDescriptionAndRequirementsAndLocationAndDatePosted(
            String title,
            String description,
            String requirements,
            String location,
            Date datePosted
    );
}

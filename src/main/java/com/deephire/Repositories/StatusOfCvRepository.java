package com.deephire.Repositories;

import com.deephire.Dto.ApplicationStatusCountDTO;
import com.deephire.Dto.JobPostingStatusDto;
import com.deephire.Models.Company;
import com.deephire.Models.JobPosting;
import com.deephire.Models.StatusOfCv;
import com.deephire.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusOfCvRepository extends JpaRepository<StatusOfCv,Long> {

    long countByJobPosting_Company(Company company);

    long count();

    @Query("SELECT s.state as state, COUNT(s) as count FROM StatusOfCv s GROUP BY s.state")
    List<ApplicationStatusCountDTO> countApplicationsByStatus();

    boolean existsByUserAndJobPosting(User user, Optional<JobPosting> jobPosting);

    List<JobPosting> findByUser(User user);



    @Query("SELECT new com.deephire.Dto.JobPostingStatusDto(" +
            "jp.id, jp.title, jp.location, jp.datePosted, c.name, c.industry, CAST(s.state AS string)) " +
            "FROM StatusOfCv s " +
            "JOIN s.jobPosting jp " +
            "JOIN jp.company c " +
            "WHERE s.user = :user")
    List<JobPostingStatusDto> findJobPostingsByUser(User user);

}

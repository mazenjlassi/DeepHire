package com.deephire.Repositories;

import com.deephire.Dto.ApplicationStatusCountDTO;
import com.deephire.Models.Company;
import com.deephire.Models.StatusOfCv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusOfCvRepository extends JpaRepository<StatusOfCv,Long> {

    long countByJobPosting_Company(Company company);

    long count();

    @Query("SELECT s.state as state, COUNT(s) as count FROM StatusOfCv s GROUP BY s.state")
    List<ApplicationStatusCountDTO> countApplicationsByStatus();
}

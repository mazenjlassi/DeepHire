package org.example.deephire.Repositories;

import org.example.deephire.models.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<Long, JobPosting> {
}

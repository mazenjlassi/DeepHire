package com.deephire.Service;

import com.deephire.Repositories.JobPostingRepository;
import com.deephire.Models.JobPosting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPostingService {
    @Autowired
    private JobPostingRepository jobPostingRepository;

    public JobPosting add(JobPosting jobPosting) { return jobPostingRepository.save(jobPosting); }

    public JobPosting find(Long id) { return jobPostingRepository.findById(id).get(); }

    public void delete(Long id) { jobPostingRepository.deleteById(id); }

    public JobPosting update(JobPosting jobPosting) { return jobPostingRepository.saveAndFlush(jobPosting); }

    public List<JobPosting> findAll() { return jobPostingRepository.findAll(); }
}

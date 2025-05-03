package com.deephire.Service;

import com.deephire.Dto.JobPostingRequestDTO;
import com.deephire.Models.Company;
import com.deephire.Repositories.JobPostingRepository;
import com.deephire.Models.JobPosting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobPostingService {
    @Autowired
    private JobPostingRepository jobPostingRepository;

    public JobPosting add(JobPosting jobPosting) {
        return jobPostingRepository.save(jobPosting);
    }

    public JobPosting find(Long id) {
        return jobPostingRepository.findById(id).get();
    }

    public void delete(Long id) {
        jobPostingRepository.deleteById(id);
    }

    public JobPosting update(JobPosting jobPosting) {
        return jobPostingRepository.saveAndFlush(jobPosting);
    }

    public List<JobPosting> findAll() {
        return jobPostingRepository.findAll();
    }

    public void deleteJobPostingByDto(JobPostingRequestDTO dto) {
        jobPostingRepository.deleteByTitleAndDescriptionAndRequirementsAndLocationAndDatePosted(
                dto.getTitle(),
                dto.getDescription(),
                dto.getRequirements(),
                dto.getLocation(),
                dto.getDatePosted()
        );
    }



    public List<Integer> getJobPostingsPerMonth(Company company) {
        List<Object[]> rawResult = jobPostingRepository.getMonthlyJobPostingsByCompanyNative(company.getId());

        List<Integer> result = new ArrayList<>();
        for (Object[] row : rawResult) {
            Number count = (Number) row[1];
            result.add(count != null ? count.intValue() : 0);
        }
        return result;
    }


}
package com.deephire.Service;

import com.deephire.Models.Profile;
import com.deephire.Repositories.EducationRepository;
import com.deephire.Models.Education;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationService {
    @Autowired
    private EducationRepository educationRepository;

    public Education add(Education education) { return educationRepository.save(education); }

    public Education find(Long id) { return educationRepository.findById(id).get(); }

    public void delete(Long id) { educationRepository.deleteById(id); }

    public Education update(Education education) { return educationRepository.saveAndFlush(education); }

    public List<Education> findAll() { return educationRepository.findAll(); }

    public List<Education> saveAllEducationWithProfile(List<Education> educationList, Profile profile) {
        educationList.forEach(education -> education.setProfile(profile));
        return educationRepository.saveAll(educationList);
    }

    public  void  delete(Education education) {
        educationRepository.delete(education);
    }

}

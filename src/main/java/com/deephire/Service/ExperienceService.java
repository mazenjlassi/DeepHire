package com.deephire.Service;

import com.deephire.Models.Profile;
import com.deephire.Repositories.ExperienceRepository;
import com.deephire.Models.Experience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceService {
    @Autowired
    private ExperienceRepository experienceRepository;

    public Experience add(Experience experience) { return experienceRepository.save(experience); }

    public Experience find(Long id) { return experienceRepository.findById(id).get(); }

    public void delete(Long id) { experienceRepository.deleteById(id); }

    public Experience update(Experience experience) { return experienceRepository.saveAndFlush(experience); }

    public List<Experience> findAll() { return experienceRepository.findAll(); }

    public List<Experience> saveAllExperiencesWithProfile(List<Experience> experiences, Profile profile) {
        experiences.forEach(experience -> experience.setProfile(profile));
        return experienceRepository.saveAll(experiences);
    }

    public  void  delete(Experience experience) {
        experienceRepository.delete(experience);
    }

}

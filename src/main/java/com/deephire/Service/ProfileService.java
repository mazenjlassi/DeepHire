package com.deephire.Service;

import com.deephire.Models.Skill;
import com.deephire.Repositories.ProfileRepository;
import com.deephire.Models.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public Profile add(Profile profile) { return profileRepository.save(profile); }

    public Profile find(Long id) { return profileRepository.findById(id).get(); }

    public void delete(Long id) { profileRepository.deleteById(id); }

    public Profile update(Profile profile) { return profileRepository.saveAndFlush(profile); }

    public List<Profile> findAll() { return profileRepository.findAll(); }


    public  void addSkill(Profile profile, Skill skill) {
        profile.getSkills().add(skill);
        profileRepository.save(profile);

    }
}

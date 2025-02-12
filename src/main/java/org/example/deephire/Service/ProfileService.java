package org.example.deephire.Service;

import org.example.deephire.Repositories.ProfileRepository;
import org.example.deephire.models.Profile;
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
}

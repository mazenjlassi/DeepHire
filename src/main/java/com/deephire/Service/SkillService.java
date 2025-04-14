package com.deephire.Service;

import com.deephire.Repositories.SkillRepository;
import com.deephire.Models.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;

    public Skill add(Skill skill) { return skillRepository.save(skill); }

    public Skill find(Long id) { return skillRepository.findById(id).get(); }

    public void delete(Long id) { skillRepository.deleteById(id); }

    public Skill update(Skill skill) { return skillRepository.saveAndFlush(skill); }

    public List<Skill> findAll() { return skillRepository.findAll(); }
}

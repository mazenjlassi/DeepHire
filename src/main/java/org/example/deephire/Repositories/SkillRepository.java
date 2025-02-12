package org.example.deephire.Repositories;

import org.example.deephire.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository< Skill, Long> {
}

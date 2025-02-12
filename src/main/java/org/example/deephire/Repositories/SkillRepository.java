package org.example.deephire.Repositories;

import org.example.deephire.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Long, Skill> {
}

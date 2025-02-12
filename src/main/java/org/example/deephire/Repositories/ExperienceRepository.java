package org.example.deephire.Repositories;

import org.example.deephire.models.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceRepository extends JpaRepository< Experience,Long> {
}

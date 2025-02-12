package org.example.deephire.Repositories;

import org.example.deephire.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Long, Profile> {
}

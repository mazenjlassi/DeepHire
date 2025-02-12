package org.example.deephire.Repositories;

import org.example.deephire.models.StatusOfCv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusOfCvRepository extends JpaRepository< StatusOfCv,Long> {
}

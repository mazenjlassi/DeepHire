package com.deephire.Repositories;

import com.deephire.Models.StatusOfCv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusOfCvRepository extends JpaRepository<StatusOfCv,Long> {
}

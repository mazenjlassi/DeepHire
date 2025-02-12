package org.example.deephire.Repositories;

import org.example.deephire.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository< User,Long> {
}

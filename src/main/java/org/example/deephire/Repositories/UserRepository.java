package org.example.deephire.Repositories;

import org.example.deephire.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {
}

package com.deephire.Repositories;

import com.deephire.Enums.ERole;

import com.deephire.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
    boolean existsByName(ERole name);

}


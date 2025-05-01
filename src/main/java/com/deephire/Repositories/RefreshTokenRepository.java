package com.deephire.Repositories;

import com.deephire.Models.RefreshToken;
import com.deephire.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Ref;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends
        JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserId(Long userId);

    void deleteByUser(User user);
}

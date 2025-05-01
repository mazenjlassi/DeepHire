package com.deephire.Repositories;

import com.deephire.Dto.UserSearchDTO;
import com.deephire.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT new com.deephire.Dto.UserSearchDTO(u.username, u.firstName, u.lastName) " +
            "FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) LIKE CONCAT(:searchText, '%')")
    List<UserSearchDTO> searchUsersByFullNameStartsWith(@Param("searchText") String searchText);
}

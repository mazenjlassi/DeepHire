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

//    @Query("SELECT new com.deephire.Dto.UserSearchDTO(u.username, u.firstName, u.lastName) " +
//            "FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) LIKE CONCAT(:searchText, '%')")
//    List<UserSearchDTO> searchUsersByFullNameStartsWith(@Param("searchText") String searchText);


    @Query("""
    SELECT DISTINCT new com.deephire.Dto.UserSearchDTO(u.username, u.firstName, u.lastName, p.headline)
    FROM User u
    LEFT JOIN u.profile p
    LEFT JOIN p.skills s
    LEFT JOIN p.experiences e
    WHERE 
        LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT(:searchText, '%'))
        OR LOWER(s.name) LIKE LOWER(CONCAT('%', :searchText, '%'))
        OR LOWER(e.companyName) LIKE LOWER(CONCAT('%', :searchText, '%'))
""")
    List<UserSearchDTO> searchUsers(@Param("searchText") String searchText);

}

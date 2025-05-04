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
    long count();

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
    @Query(value = """
        SELECT m.month_num AS month, 
               COALESCE(COUNT(u.id), 0) AS count
        FROM (
            SELECT 1 AS month_num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION
            SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION
            SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12
        ) AS m
        LEFT JOIN users u ON MONTH(u.created_at) = m.month_num 
        GROUP BY m.month_num
        ORDER BY m.month_num
    """, nativeQuery = true)
    List<Object[]> getUsersPerMonth();





}

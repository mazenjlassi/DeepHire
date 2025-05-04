package com.deephire.Repositories;

import com.deephire.Enums.ERole;
import com.deephire.Models.Company;
import com.deephire.Models.RHCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public interface RHCompanyRepository extends JpaRepository<RHCompany, Long> {
    long countByCompany(Company company);

    long count();

    @Query(value = """
        SELECT months.month,
               IFNULL(COUNT(u.id), 0) AS count
        FROM (
            SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION
            SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION
            SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12
        ) AS months
        LEFT JOIN users u ON MONTH(u.created_at) = months.month
                         AND YEAR(u.created_at) = :year
                         AND u.company_id = :companyId
                         AND u.dtype = 'RHCompany'
        LEFT JOIN user_roles ur ON u.id = ur.user_id
        LEFT JOIN roles r ON ur.role_id = r.id
        WHERE r.name = :roleName
        GROUP BY months.month
        ORDER BY months.month
    """, nativeQuery = true)
    List<Object[]> getUserCountPerMonthByRole(
            @Param("companyId") Long companyId,
            @Param("year") int year,
            @Param("roleName") String roleName
    );

    default List<Integer> getRecruiterCountPerMonth(Long companyId, int year) {
        List<Object[]> results = getUserCountPerMonthByRole(
                companyId,
                year,
                ERole.ROLE_RECRUITER.name()
        );
        // Ensure all 12 months are included, even if some are missing
        List<Integer> counts = new ArrayList<>(Collections.nCopies(12, 0));
        for (Object[] row : results) {
            int month = ((Number) row[0]).intValue();
            int count = ((Number) row[1]).intValue();
            counts.set(month - 1, count);
        }
        return counts;
    }
}
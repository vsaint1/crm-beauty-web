package br.com.crm.beauty.web.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.crm.beauty.web.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  @Query("SELECT e FROM Employee e WHERE e.company.id = ?1")
  Page<Employee> findByCompanyIdPaged(UUID id, Pageable pageable);

  @Query("""
        SELECT e
        FROM Employee e
        WHERE e.company.id = :companyId
          AND (
            LOWER(e.user.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(e.user.lastName) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(e.user.email) LIKE LOWER(CONCAT('%', :query, '%'))
          )
      """)
  Employee findByCompanyIdAndQuery(UUID companyId, String query);

  Employee findByUserIdAndCompanyId(Long userId, UUID companyId);

  @Query("SELECT e FROM Employee e WHERE e.user.id = ?1")
  Employee findEmployeeByUserId(Long id);

  @Query("SELECT e FROM Employee e WHERE e.user.email = ?1 AND e.isActive = true")
  Employee findByUserEmail(String email);
  @Query("""
          SELECT e
          FROM Employee e
          LEFT JOIN FETCH e.user u
          JOIN FETCH e.company c
          WHERE (c is NULL OR e.isActive = true) AND e.user.email = ?1
      """)
  Employee findActiveEmployeeWithUserAndCompanyByUserEmail(String email);

}

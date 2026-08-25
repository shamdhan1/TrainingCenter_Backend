package com.trainingcenter.repository;

import com.trainingcenter.entity.Student;
import com.trainingcenter.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserUserId(Long userId);
    Optional<Student> findByUserUsername(String username);
    boolean existsByRegistrationNo(String registrationNo);

    @Query("SELECT s FROM Student s WHERE " +
           "(:query IS NULL OR :query = '' OR " +
           " LOWER(s.registrationNo) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(s.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(s.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(s.mobile) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(s.email) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(:centerId IS NULL OR s.center.centerId = :centerId)")
    Page<Student> searchStudents(@Param("query") String query, 
                                 @Param("status") UserStatus status, 
                                 @Param("centerId") Long centerId, 
                                 Pageable pageable);

    long countByStatus(UserStatus status);
}

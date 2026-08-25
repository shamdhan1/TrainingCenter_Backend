package com.trainingcenter.repository;

import com.trainingcenter.entity.Trainer;
import com.trainingcenter.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUserUserId(Long userId);
    Optional<Trainer> findByUserUsername(String username);
    boolean existsByEmployeeCode(String employeeCode);
    List<Trainer> findByCenterCenterIdAndStatus(Long centerId, UserStatus status);

    @Query("SELECT t FROM Trainer t WHERE " +
           "(:query IS NULL OR :query = '' OR " +
           " LOWER(t.employeeCode) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(t.specialization) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:centerId IS NULL OR t.center.centerId = :centerId)")
    Page<Trainer> searchTrainers(@Param("query") String query, 
                                 @Param("status") UserStatus status, 
                                 @Param("centerId") Long centerId, 
                                 Pageable pageable);

    long countByStatus(UserStatus status);
}

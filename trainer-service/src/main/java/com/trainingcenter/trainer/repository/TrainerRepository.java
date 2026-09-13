package com.trainingcenter.trainer.repository;

import com.trainingcenter.trainer.entity.Trainer;
import com.trainingcenter.trainer.enums.UserStatus;
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

    boolean existsByEmployeeCode(String employeeCode);

    Optional<Trainer> findByEmployeeCode(String employeeCode);

    Optional<Trainer> findByUserId(Long userId);

    List<Trainer> findByCenterId(Long centerId);

    Page<Trainer> findByCenterId(Long centerId, Pageable pageable);

    @Query("SELECT t FROM Trainer t WHERE " +
            "(:query IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(t.employeeCode) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(t.email) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(t.specialization) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:centerId IS NULL OR t.centerId = :centerId)")
    Page<Trainer> searchTrainers(@Param("query") String query,
                                 @Param("status") UserStatus status,
                                 @Param("centerId") Long centerId,
                                 Pageable pageable);
}

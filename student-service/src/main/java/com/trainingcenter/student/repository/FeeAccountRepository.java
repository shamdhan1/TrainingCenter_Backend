package com.trainingcenter.student.repository;

import com.trainingcenter.student.entity.FeeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeeAccountRepository extends JpaRepository<FeeAccount, Long> {
    Optional<FeeAccount> findByEnrollmentEnrollmentId(Long enrollmentId);
}

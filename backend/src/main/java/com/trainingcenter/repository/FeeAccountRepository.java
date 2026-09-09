package com.trainingcenter.repository;

import com.trainingcenter.entity.FeeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface FeeAccountRepository extends JpaRepository<FeeAccount, Long> {

    Optional<FeeAccount> findByEnrollmentEnrollmentId(Long enrollmentId);

    @Query("SELECT SUM(f.paidAmount) FROM FeeAccount f")
    BigDecimal sumTotalRevenue();

    @Query("SELECT SUM(f.remainingAmount) FROM FeeAccount f")
    BigDecimal sumTotalPendingFees();

    @Query("SELECT SUM(f.remainingAmount) FROM FeeAccount f WHERE f.enrollment.center.centerId = :centerId")
    BigDecimal sumPendingFeesByCenterId(@Param("centerId") Long centerId);
}

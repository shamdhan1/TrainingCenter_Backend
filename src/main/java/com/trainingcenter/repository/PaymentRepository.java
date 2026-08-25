package com.trainingcenter.repository;

import com.trainingcenter.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByFeeAccountEnrollmentEnrollmentId(Long enrollmentId);
    Page<Payment> findByFeeAccountEnrollmentEnrollmentId(Long enrollmentId, Pageable pageable);
    boolean existsByReceiptNo(String receiptNo);
}

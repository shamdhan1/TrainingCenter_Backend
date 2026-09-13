package com.trainingcenter.student.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionResponse {
    private Long studentId;
    private Long enrollmentId;
    private Long feeAccountId;
    private Long paymentId;
    private String registrationNo;
    private String studentName;
    private String centerName;
    private String courseName;
    private BigDecimal totalFee;
    private BigDecimal discount;
    private BigDecimal netFee;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;
    private String paymentStatus;
    private String receiptNo;
    private String message;
}

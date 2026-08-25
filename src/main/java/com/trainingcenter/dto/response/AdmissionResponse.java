package com.trainingcenter.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
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
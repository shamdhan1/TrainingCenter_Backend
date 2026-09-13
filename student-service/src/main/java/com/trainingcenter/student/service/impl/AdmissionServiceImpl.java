package com.trainingcenter.student.service.impl;

import com.trainingcenter.student.dto.request.AdmissionRequest;
import com.trainingcenter.student.dto.response.AdmissionResponse;
import com.trainingcenter.student.entity.Enrollment;
import com.trainingcenter.student.entity.FeeAccount;
import com.trainingcenter.student.entity.Payment;
import com.trainingcenter.student.entity.Student;
import com.trainingcenter.student.enums.EnrollmentStatus;
import com.trainingcenter.student.enums.PaymentMode;
import com.trainingcenter.student.enums.PaymentStatus;
import com.trainingcenter.student.enums.UserStatus;
import com.trainingcenter.student.exception.ConflictException;
import com.trainingcenter.student.repository.EnrollmentRepository;
import com.trainingcenter.student.repository.FeeAccountRepository;
import com.trainingcenter.student.repository.PaymentRepository;
import com.trainingcenter.student.repository.StudentRepository;
import com.trainingcenter.student.service.AdmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdmissionServiceImpl implements AdmissionService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final FeeAccountRepository feeAccountRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public AdmissionResponse createAdmission(AdmissionRequest request) {
        if (studentRepository.existsByRegistrationNo(request.getRegistrationNo())) {
            throw new ConflictException("Registration number already exists: " + request.getRegistrationNo());
        }

        // 1. Create Student
        Student student = Student.builder()
                .centerId(request.getCenterId())
                .registrationNo(request.getRegistrationNo().toUpperCase())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .mobile(request.getMobile())
                .email(request.getEmail())
                .qualification(request.getQualification())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .parentName(request.getFatherName() != null ? request.getFatherName() : request.getGuardianName())
                .parentMobile(request.getGuardianMobile())
                .admissionDate(request.getRegistrationDate() != null ? request.getRegistrationDate() : LocalDate.now())
                .status(request.getStatus() != null ? UserStatus.valueOf(request.getStatus().toUpperCase()) : UserStatus.ACTIVE)
                .aadhaarNo(request.getAadhaarNo())
                .build();
        Student savedStudent = studentRepository.save(student);

        // 2. Create Enrollment
        Enrollment enrollment = Enrollment.builder()
                .student(savedStudent)
                .batchId(request.getBatchId() != null ? request.getBatchId() : 1L)
                .courseId(request.getCourseId())
                .trainerId(request.getTrainerId())
                .centerId(request.getCenterId())
                .enrollmentDate(request.getStartDate() != null ? request.getStartDate() : LocalDate.now())
                .status(EnrollmentStatus.ACTIVE)
                .build();
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // 3. Create FeeAccount
        BigDecimal totalFee = request.getTotalFee() != null ? request.getTotalFee() : BigDecimal.valueOf(10000);
        BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;
        BigDecimal paidAmount = request.getInitialPayment() != null ? request.getInitialPayment() : BigDecimal.ZERO;
        BigDecimal netFee = totalFee.subtract(discount);
        BigDecimal remainingAmount = netFee.subtract(paidAmount);

        PaymentStatus paymentStatus = PaymentStatus.PENDING;
        if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            paymentStatus = PaymentStatus.PAID;
        } else if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            paymentStatus = PaymentStatus.PARTIAL;
        }

        FeeAccount feeAccount = FeeAccount.builder()
                .enrollment(savedEnrollment)
                .totalFee(totalFee)
                .discount(discount)
                .netFee(netFee)
                .paidAmount(paidAmount)
                .remainingAmount(remainingAmount)
                .dueDate(request.getFeeDueDate())
                .paymentStatus(paymentStatus)
                .build();
        FeeAccount savedFeeAccount = feeAccountRepository.save(feeAccount);

        // 4. Record Initial Payment if any
        Payment savedPayment = null;
        String receiptNo = null;
        if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            receiptNo = "REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            PaymentMode mode = PaymentMode.CASH;
            if (request.getPaymentMode() != null) {
                try {
                    mode = PaymentMode.valueOf(request.getPaymentMode().toUpperCase());
                } catch (Exception ignored) {
                }
            }

            Payment payment = Payment.builder()
                    .feeAccount(savedFeeAccount)
                    .receiptNo(receiptNo)
                    .paymentDate(LocalDate.now())
                    .amount(paidAmount)
                    .paymentMode(mode)
                    .transactionReference(request.getTransactionReference())
                    .remarks(request.getRemarks())
                    .createdBy("SYSTEM")
                    .build();
            savedPayment = paymentRepository.save(payment);
        }

        return AdmissionResponse.builder()
                .studentId(savedStudent.getStudentId())
                .enrollmentId(savedEnrollment.getEnrollmentId())
                .feeAccountId(savedFeeAccount.getFeeAccountId())
                .paymentId(savedPayment != null ? savedPayment.getPaymentId() : null)
                .registrationNo(savedStudent.getRegistrationNo())
                .studentName(savedStudent.getFirstName() + " " + savedStudent.getLastName())
                .totalFee(totalFee)
                .discount(discount)
                .netFee(netFee)
                .paidAmount(paidAmount)
                .remainingAmount(remainingAmount)
                .paymentStatus(paymentStatus.name())
                .receiptNo(receiptNo)
                .message("Student admission completed successfully")
                .build();
    }
}

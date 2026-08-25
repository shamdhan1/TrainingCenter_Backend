package com.trainingcenter.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trainingcenter.dto.request.AdmissionRequest;
import com.trainingcenter.dto.response.AdmissionResponse;
import com.trainingcenter.entity.Center;
import com.trainingcenter.entity.Course;
import com.trainingcenter.entity.Enrollment;
import com.trainingcenter.entity.FeeAccount;
import com.trainingcenter.entity.Payment;
import com.trainingcenter.entity.Role;
import com.trainingcenter.entity.Student;
import com.trainingcenter.entity.Trainer;
import com.trainingcenter.entity.UserAccount;
import com.trainingcenter.enums.EnrollmentStatus;
import com.trainingcenter.enums.PaymentMode;
import com.trainingcenter.enums.PaymentStatus;
import com.trainingcenter.enums.RoleName;
import com.trainingcenter.enums.UserStatus;
import com.trainingcenter.repository.CenterRepository;
import com.trainingcenter.repository.CourseRepository;
import com.trainingcenter.repository.EnrollmentRepository;
import com.trainingcenter.repository.FeeAccountRepository;
import com.trainingcenter.repository.PaymentRepository;
import com.trainingcenter.repository.RoleRepository;
import com.trainingcenter.repository.StudentRepository;
import com.trainingcenter.repository.TrainerRepository;
import com.trainingcenter.repository.UserAccountRepository;
import com.trainingcenter.service.AdmissionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdmissionServiceImpl implements AdmissionService {

    private final StudentRepository studentRepository;
    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;

    private final CenterRepository centerRepository;
    private final CourseRepository courseRepository;
    private final TrainerRepository trainerRepository;

    private final EnrollmentRepository enrollmentRepository;
    private final FeeAccountRepository feeAccountRepository;
    private final PaymentRepository paymentRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public AdmissionResponse createAdmission(AdmissionRequest request) {

        String username = request.getUsername().trim().toLowerCase();
        if (userAccountRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists: " + username);
        }

        String email = request.getEmail().trim().toLowerCase();

        if (userAccountRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists: " + email);
        }

        String registrationNo = request.getRegistrationNo()
                .trim()
                .toUpperCase();

        if (studentRepository.existsByRegistrationNo(
                registrationNo)) {

            throw new RuntimeException(
                    "Registration number already exists: "
                            + registrationNo);
        }

        Center center = centerRepository.findById(
                request.getCenterId()).orElseThrow(
                        () -> new RuntimeException(
                                "Training center not found"));

        Course course = courseRepository.findById(
                request.getCourseId()).orElseThrow(
                        () -> new RuntimeException(
                                "Course not found"));
        if (!course.getCenter()
                .getCenterId()
                .equals(center.getCenterId())) {

            throw new RuntimeException(
                    "Selected course does not belong to selected center");
        }

        // =====================================================
        // 7. LOAD TRAINER
        // =====================================================

        Trainer trainer = null;

        if (request.getTrainerId() != null) {

            trainer = trainerRepository.findById(
                    request.getTrainerId()).orElseThrow(
                            () -> new RuntimeException(
                                    "Trainer not found"));

            if (!trainer.getCenter()
                    .getCenterId()
                    .equals(center.getCenterId())) {

                throw new RuntimeException(
                        "Trainer does not belong to selected center");
            }
        }

        // =====================================================
        // 8. FIND STUDENT ROLE
        // =====================================================

        Role role = roleRepository.findByRoleName(RoleName.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // =====================================================
        // 9. CREATE USER ACCOUNT
        // =====================================================

        UserAccount user = UserAccount.builder()

                .username(username)

                .passwordHash(
                        passwordEncoder.encode(
                                request.getPassword()))

                .email(email)

                .mobile(request.getMobile())

                .status(UserStatus.ACTIVE)

                .center(center)

                .roles(
                        new HashSet<>(
                                Set.of(role)))

                .build();

        UserAccount savedUser = userAccountRepository.save(user);

        Student student = Student.builder()

                .user(savedUser)

                .center(center)

                .registrationNo(registrationNo)

                .firstName(request.getFirstName())

                .lastName(request.getLastName())

                .dateOfBirth(
                        request.getDateOfBirth())

                .gender(request.getGender())

                .mobile(request.getMobile())

                .email(email)

                .qualification(
                        request.getQualification())

                .address(
                        request.getAddress())

                .fatherName(
                        request.getFatherName())

                .motherName(
                        request.getMotherName())

                .guardianName(
                        request.getGuardianName())

                .guardianMobile(
                        request.getGuardianMobile())

                .aadhaarNo(
                        request.getAadhaarNo())

                .city(
                        request.getCity())

                .state(
                        request.getState())

                .pincode(
                        request.getPincode())

                .registrationDate(
                        request.getRegistrationDate() != null
                                ? request.getRegistrationDate()
                                : LocalDate.now())

                .status(UserStatus.ACTIVE)

                .build();

        Student savedStudent = studentRepository.save(student);

        Enrollment enrollment = Enrollment.builder()

                .student(savedStudent)

                .course(course)

                .trainer(trainer)

                .center(center)

                .registrationDate(
                        request.getRegistrationDate() != null
                                ? request.getRegistrationDate()
                                : LocalDate.now())

                .startDate(
                        request.getStartDate())

                .expectedEndDate(
                        request.getExpectedEndDate())

                .status(
                        EnrollmentStatus.ACTIVE)

                .remarks(
                        request.getRemarks())

                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        BigDecimal totalFee = course.getTotalFee() != null
                ? course.getTotalFee()
                : BigDecimal.ZERO;

        BigDecimal discount = request.getDiscount() != null
                ? request.getDiscount()
                : BigDecimal.ZERO;

        BigDecimal initialPayment = request.getInitialPayment() != null
                ? request.getInitialPayment()
                : BigDecimal.ZERO;

        if (discount.compareTo(totalFee) > 0) {

            throw new RuntimeException(
                    "Discount cannot be greater than course fee");
        }

        BigDecimal netFee = totalFee.subtract(discount);

        if (initialPayment.compareTo(netFee) > 0) {

            throw new RuntimeException(
                    "Initial payment cannot be greater than net fee");
        }

        BigDecimal remaining = netFee.subtract(initialPayment);

        PaymentStatus paymentStatus;

        if (remaining.compareTo(BigDecimal.ZERO) == 0) {

            paymentStatus = PaymentStatus.PAID;

        } else if (initialPayment.compareTo(BigDecimal.ZERO) > 0) {

            paymentStatus = PaymentStatus.PARTIAL;

        } else {

            paymentStatus = PaymentStatus.PENDING;
        }

        // =====================================================
        // 14. CREATE FEE ACCOUNT
        // =====================================================

        FeeAccount feeAccount = FeeAccount.builder()

                .enrollment(savedEnrollment)

                .totalFee(totalFee)

                .discount(discount)

                .netFee(netFee)

                .paidAmount(initialPayment)

                .remainingAmount(remaining)

                .dueDate(
                        request.getFeeDueDate())

                .paymentStatus(paymentStatus)

                .build();

        FeeAccount savedFeeAccount = feeAccountRepository.save(feeAccount);

        Payment savedPayment = null;

        if (initialPayment.compareTo(
                BigDecimal.ZERO) > 0) {

            PaymentMode paymentMode;

            try {

                paymentMode = PaymentMode.valueOf(
                        request.getPaymentMode()
                                .toUpperCase());

            } catch (Exception e) {

                throw new RuntimeException(
                        "Invalid payment mode");
            }

            Payment payment = Payment.builder()
                    .feeAccount(savedFeeAccount)
                    .receiptNo(generateReceiptNumber()).paymentDate(LocalDate.now())
                    .amount(initialPayment)
                    .paymentMode(paymentMode).transactionReference(request.getTransactionReference())
                    .remarks("Initial admission payment")
                    .build();
            savedPayment = paymentRepository.save(payment);
        }

        return AdmissionResponse.builder()

                .studentId(
                        savedStudent.getStudentId())

                .enrollmentId(
                        savedEnrollment.getEnrollmentId())

                .feeAccountId(
                        savedFeeAccount.getFeeAccountId())

                .paymentId(
                        savedPayment != null
                                ? savedPayment.getPaymentId()
                                : null)

                .registrationNo(
                        savedStudent.getRegistrationNo())

                .studentName(
                        savedStudent.getFirstName()
                                + " "
                                + savedStudent.getLastName())

                .centerName(
                        center.getName())

                .courseName(
                        course.getCourseName())

                .totalFee(totalFee)

                .discount(discount)

                .netFee(netFee)

                .paidAmount(initialPayment)

                .remainingAmount(remaining)

                .paymentStatus(
                        paymentStatus.name())

                .receiptNo(
                        savedPayment != null
                                ? savedPayment.getReceiptNo()
                                : null)

                .message(
                        "Admission completed successfully")

                .build();
    }

    private String generateReceiptNumber() {

        String timestamp = LocalDateTime.now()
                .format(
                        DateTimeFormatter
                                .ofPattern(
                                        "yyyyMMddHHmmss"));

        String random = UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();

        return "RCPT-"
                + timestamp
                + "-"
                + random;
    }

}

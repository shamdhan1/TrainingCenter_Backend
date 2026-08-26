package com.trainingcenter.entity;

import com.trainingcenter.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "trainers", indexes = {
    @Index(name = "idx_trainer_emp_code", columnList = "employee_code"),
    @Index(name = "idx_trainer_email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainer_id")
    private Long trainerId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserAccount user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", referencedColumnName = "center_id", nullable = false)
    private Center center;

    @Column(name = "employee_code", unique = true, nullable = false, length = 30)
    private String employeeCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "mobile", length = 15)
    private String mobile;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "specialization", length = 100)
    private String specialization;

    @Column(name = "qualification", length = 100)
    private String qualification;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UserStatus status;

    // ==========================================
    // EXTENDED ONBOARDING & PROFILE FIELDS
    // ==========================================

    @Column(name = "gender", length = 15)
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "aadhaar_no", length = 12)
    private String aadhaarNo;

    @Column(name = "pan_no", length = 10)
    private String panNo;

    @Column(name = "alternative_mobile", length = 15)
    private String alternativeMobile;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Column(name = "designation", length = 50)
    private String designation;

    @Column(name = "contract_type", length = 20)
    private String contractType; // e.g. FULL_TIME, PART_TIME, CONTRACT, GUEST

    @Column(name = "salary", precision = 10, scale = 2)
    private BigDecimal salary;

    // Payroll Bank details
    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "bank_account_number", length = 30)
    private String bankAccountNumber;

    @Column(name = "ifsc_code", length = 20)
    private String ifscCode;

    // Bio profile
    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

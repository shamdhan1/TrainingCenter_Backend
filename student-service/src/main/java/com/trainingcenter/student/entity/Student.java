package com.trainingcenter.student.entity;

import com.trainingcenter.student.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "students", indexes = {
        @Index(name = "idx_student_reg_no", columnList = "registration_no"),
        @Index(name = "idx_student_mobile", columnList = "mobile"),
        @Index(name = "idx_student_email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    // Cross-service reference: references User in auth-service
    @Column(name = "user_id")
    private Long userId;

    // Cross-service reference: references Center in admin-service
    @Column(name = "center_id", nullable = false)
    private Long centerId;

    @Column(name = "registration_no", unique = true, nullable = false, length = 30)
    private String registrationNo;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "dob")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 15)
    private String gender;

    @Column(name = "mobile", length = 15)
    private String mobile;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Column(name = "parent_name", length = 100)
    private String parentName;

    @Column(name = "parent_mobile", length = 15)
    private String parentMobile;

    @Column(name = "qualification", length = 100)
    private String qualification;

    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "blood_group", length = 5)
    private String bloodGroup;

    @Column(name = "aadhaar_no", length = 12)
    private String aadhaarNo;

    @Column(name = "guardian_occupation", length = 50)
    private String guardianOccupation;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

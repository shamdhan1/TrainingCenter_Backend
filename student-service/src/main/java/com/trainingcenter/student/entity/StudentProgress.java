package com.trainingcenter.student.entity;

import com.trainingcenter.student.enums.ProgressStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_progress", uniqueConstraints = {
    @UniqueConstraint(name = "uc_enrollment_content", columnNames = {"enrollment_id", "content_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Long progressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", referencedColumnName = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    // Cross-service reference: references CourseContent in admin-service
    @Column(name = "content_id", nullable = false)
    private Long contentId;

    @Column(name = "progress_percent", nullable = false)
    private Integer progressPercent;

    @Column(name = "completed_date")
    private LocalDate completedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProgressStatus status;

    @Column(name = "trainer_remarks", length = 255)
    private String trainerRemarks;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

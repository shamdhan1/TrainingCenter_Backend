package com.trainingcenter.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", referencedColumnName = "assessment_id", nullable = false)
    private Assessment assessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", referencedColumnName = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(name = "marks_obtained", nullable = false)
    private Double marksObtained;

    @Column(name = "percentage")
    private Double percentage;

    @Column(name = "grade", length = 5)
    private String grade;

    @Column(name = "pass_fail", length = 10)
    private String passFail;

    @Column(name = "trainer_feedback", length = 255)
    private String trainerFeedback;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void calculatePercentageAndGrade(Double maxMarks, Double passingMarks) {
        if (this.marksObtained == null || maxMarks == null || maxMarks == 0.0) {
            this.percentage = 0.0;
            this.grade = "D";
            this.passFail = "FAIL";
            return;
        }

        this.percentage = (this.marksObtained / maxMarks) * 100.0;
        
        // Pass Fail logic
        if (this.marksObtained >= passingMarks) {
            this.passFail = "PASS";
        } else {
            this.passFail = "FAIL";
        }

        // Grade logic: 90+ = A+, 80-89 = A, 70-79 = B+, 60-69 = B, 50-59 = C, Below 50 = D
        if (this.percentage >= 90.0) {
            this.grade = "A+";
        } else if (this.percentage >= 80.0) {
            this.grade = "A";
        } else if (this.percentage >= 70.0) {
            this.grade = "B+";
        } else if (this.percentage >= 60.0) {
            this.grade = "B";
        } else if (this.percentage >= 50.0) {
            this.grade = "C";
        } else {
            this.grade = "D";
        }
    }
}

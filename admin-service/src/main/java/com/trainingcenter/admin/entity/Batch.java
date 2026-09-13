package com.trainingcenter.admin.entity;

import com.trainingcenter.admin.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "batches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batchId;

    @Column(nullable = false, unique = true, length = 20)
    private String batchCode;

    @Column(nullable = false, length = 100)
    private String batchName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    private Center center;

    // Cross-service reference: references Trainer in trainer-service
    @Column(name = "trainer_id")
    private Long trainerId;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(length = 50)
    private String timings;

    @Column(nullable = false)
    private Integer maxStudents;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CourseStatus status = CourseStatus.ACTIVE;
}

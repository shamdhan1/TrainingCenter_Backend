package com.trainingcenter.admin.entity;

import com.trainingcenter.admin.enums.ContentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course_contents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private Integer moduleNumber;

    @Column(nullable = false, length = 150)
    private String topicTitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ContentType contentType;

    @Column(length = 500)
    private String resourceLink;
}
